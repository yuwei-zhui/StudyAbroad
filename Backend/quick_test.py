#!/usr/bin/env python3
"""
Quick test to verify JSON response format
"""

import requests
import json

def test_json_response():
    """Test that the server returns proper JSON"""
    url = "http://localhost:5000/chat"
    
    test_data = {
        "message": "hello",
        "userId": "test_user",
        "academicProfile": "Test profile"
    }
    
    try:
        print("Testing JSON response format...")
        print(f"URL: {url}")
        print(f"Request: {json.dumps(test_data, indent=2)}")
        print("-" * 50)
        
        headers = {'Content-Type': 'application/json'}
        response = requests.post(url, json=test_data, headers=headers, timeout=30)
        
        print(f"Status Code: {response.status_code}")
        print(f"Content-Type: {response.headers.get('Content-Type', 'Not set')}")
        print(f"Response Headers: {dict(response.headers)}")
        print("-" * 50)
        
        if response.status_code == 200:
            try:
                response_json = response.json()
                print("✅ Response is valid JSON:")
                print(json.dumps(response_json, indent=2))
                
                # Check required fields
                if 'response' in response_json and 'status' in response_json:
                    print("✅ Response has required fields")
                    return True
                else:
                    print("❌ Response missing required fields")
                    return False
                    
            except json.JSONDecodeError as e:
                print(f"❌ Response is not valid JSON: {e}")
                print(f"Raw response: {response.text}")
                return False
        else:
            print(f"❌ HTTP Error: {response.status_code}")
            print(f"Response: {response.text}")
            return False
            
    except requests.exceptions.ConnectionError:
        print("❌ Connection Error: Server not running")
        return False
    except Exception as e:
        print(f"❌ Error: {e}")
        return False

if __name__ == "__main__":
    print("🧪 Quick JSON Response Test")
    print("=" * 50)
    
    success = test_json_response()
    
    print("=" * 50)
    if success:
        print("🎉 JSON response test PASSED!")
    else:
        print("❌ JSON response test FAILED!") 