#!/usr/bin/env python3
"""
Enhanced test script to verify the Flask server is working correctly
"""

import requests
import json
import time

def test_health_check():
    """Test the health check endpoint"""
    url = "http://localhost:5000/health"
    
    print("Testing health check endpoint...")
    print(f"URL: {url}")
    print("-" * 50)
    
    try:
        response = requests.get(url, timeout=10)
        print(f"Health Check Status Code: {response.status_code}")
        
        if response.status_code == 200:
            health_data = response.json()
            print("Health Check Response:")
            print(json.dumps(health_data, indent=2))
            return health_data.get('status') == 'healthy'
        else:
            print(f"Health check failed: {response.text}")
            return False
            
    except Exception as e:
        print(f"❌ Health check error: {str(e)}")
        return False

def test_chat_json():
    """Test the chat endpoint with JSON data"""
    url = "http://localhost:5000/chat"
    
    # Test data - simulating Android app request
    test_data = {
        "message": "Hello, can you help me with studying abroad in Canada?",
        "userId": "test_user_123",
        "academicProfile": "Name: Test User\nMajor: Computer Science\nGPA: 3.8\nTarget Degree: Master's\nPreferred Country: Canada"
    }
    
    print("\nTesting JSON chat endpoint...")
    print(f"URL: {url}")
    print(f"Request data: {json.dumps(test_data, indent=2)}")
    print("-" * 50)
    
    try:
        # Send POST request with JSON data
        headers = {'Content-Type': 'application/json'}
        response = requests.post(url, json=test_data, headers=headers, timeout=60)
        
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Headers: {dict(response.headers)}")
        print("-" * 50)
        
        if response.status_code == 200:
            try:
                response_json = response.json()
                print("Response JSON:")
                print(json.dumps(response_json, indent=2))
                
                ai_response = response_json.get('response', '')
                if ai_response:
                    print(f"\n✅ AI Response received ({len(ai_response)} characters):")
                    print(f"'{ai_response[:100]}{'...' if len(ai_response) > 100 else ''}'")
                    return True
                else:
                    print("❌ No AI response in JSON")
                    return False
                    
            except json.JSONDecodeError:
                print("❌ Response is not valid JSON:")
                print(response.text)
                return False
        else:
            print(f"❌ Error: {response.status_code}")
            print(f"Response: {response.text}")
            return False
            
    except requests.exceptions.ConnectionError:
        print("❌ Connection Error: Could not connect to the server.")
        print("Make sure the Flask server is running on localhost:5000")
        return False
    except requests.exceptions.Timeout:
        print("❌ Timeout Error: Server took too long to respond.")
        return False
    except Exception as e:
        print(f"❌ Unexpected Error: {str(e)}")
        return False

def test_chat_form_data():
    """Test with simple form data (backward compatibility)"""
    url = "http://localhost:5000/chat"
    
    print("\n" + "="*60)
    print("Testing form data request (backward compatibility)...")
    
    try:
        # Send POST request with form data
        form_data = {'userMessage': 'What are the best universities for computer science in Australia?'}
        response = requests.post(url, data=form_data, timeout=30)
        
        print(f"Form Data Response Status Code: {response.status_code}")
        if response.status_code == 200:
            print("Form Data Response Text:")
            print(f"'{response.text[:200]}{'...' if len(response.text) > 200 else ''}'")
            return True
        else:
            print(f"❌ Form data error: {response.text}")
            return False
            
    except Exception as e:
        print(f"❌ Form data test error: {str(e)}")
        return False

def test_error_handling():
    """Test error handling with invalid requests"""
    url = "http://localhost:5000/chat"
    
    print("\n" + "="*60)
    print("Testing error handling...")
    
    # Test empty message
    try:
        empty_data = {"message": "", "userId": "test", "academicProfile": ""}
        response = requests.post(url, json=empty_data, timeout=10)
        
        if response.status_code == 400:
            print("✅ Empty message correctly rejected")
        else:
            print(f"❌ Empty message handling failed: {response.status_code}")
            
    except Exception as e:
        print(f"❌ Error handling test failed: {str(e)}")

def test_multiple_requests():
    """Test multiple consecutive requests"""
    url = "http://localhost:5000/chat"
    
    print("\n" + "="*60)
    print("Testing multiple consecutive requests...")
    
    test_messages = [
        "What is the application deadline for Canadian universities?",
        "How much does it cost to study in Canada?",
        "What are the English language requirements?"
    ]
    
    success_count = 0
    
    for i, message in enumerate(test_messages, 1):
        try:
            test_data = {
                "message": message,
                "userId": f"test_user_{i}",
                "academicProfile": "Major: Engineering\nGPA: 3.5"
            }
            
            print(f"\nRequest {i}: {message}")
            response = requests.post(url, json=test_data, timeout=30)
            
            if response.status_code == 200:
                response_json = response.json()
                ai_response = response_json.get('response', '')
                if ai_response:
                    print(f"✅ Response {i} received ({len(ai_response)} chars)")
                    success_count += 1
                else:
                    print(f"❌ Response {i} empty")
            else:
                print(f"❌ Request {i} failed: {response.status_code}")
                
        except Exception as e:
            print(f"❌ Request {i} error: {str(e)}")
    
    print(f"\nMultiple requests test: {success_count}/{len(test_messages)} successful")
    return success_count == len(test_messages)

def main():
    print("=" * 60)
    print("🧪 StudyAbroad AI Backend Server Test Suite")
    print("=" * 60)
    
    # Test health check first
    if not test_health_check():
        print("\n❌ Health check failed. Please ensure:")
        print("1. Flask server is running (python start_server.py)")
        print("2. Ollama server is running (ollama serve)")
        return
    
    # Test main chat functionality
    json_success = test_chat_json()
    form_success = test_chat_form_data()
    
    # Test error handling
    test_error_handling()
    
    # Test multiple requests
    multiple_success = test_multiple_requests()
    
    # Summary
    print("\n" + "=" * 60)
    print("📊 TEST SUMMARY")
    print("=" * 60)
    print(f"Health Check: {'✅ PASS' if True else '❌ FAIL'}")
    print(f"JSON Chat: {'✅ PASS' if json_success else '❌ FAIL'}")
    print(f"Form Data Chat: {'✅ PASS' if form_success else '❌ FAIL'}")
    print(f"Multiple Requests: {'✅ PASS' if multiple_success else '❌ FAIL'}")
    
    if json_success and form_success:
        print("\n🎉 All core tests passed! Your AI backend is working correctly.")
        print("📱 You can now test with the Android app.")
    else:
        print("\n⚠️  Some tests failed. Please check the server logs.")
    
    print("=" * 60)

if __name__ == "__main__":
    main() 