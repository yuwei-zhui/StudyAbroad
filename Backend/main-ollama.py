from flask import Flask, request, Response, jsonify
from flask_cors import CORS
import requests
import argparse
import json
import time

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Ollama API endpoint and model configuration
OLLAMA_API_URL = "http://localhost:11434/api/generate"
MODEL = "llama3.2:latest"

def check_ollama_server():
    """Check if the Ollama server is running."""
    try:
        response = requests.get("http://localhost:11434", timeout=5)
        if response.status_code == 200:
            print("Ollama server is running.")
            return True
        else:
            print("Ollama server is not responding correctly.")
            return False
    except requests.ConnectionError:
        print("Error: Could not connect to Ollama server. Ensure it is running with 'ollama serve'.")
        return False
    except Exception as e:
        print(f"Error checking Ollama server: {e}")
        return False

@app.route('/')
def index():
    return jsonify({"message": "Welcome to the StudyAbroad AI Chatbot API!", "status": "running"})

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    ollama_status = check_ollama_server()
    return jsonify({
        "status": "healthy" if ollama_status else "unhealthy",
        "ollama_server": "running" if ollama_status else "not running",
        "model": MODEL
    })

@app.route('/chat', methods=['POST'])
def chat():
    try:
        # Determine if this is a JSON request (from Android app)
        is_json_request = request.is_json or request.content_type == 'application/json'
        
        # Handle both form data and JSON requests
        if is_json_request:
            # JSON request from Android app
            data = request.get_json()
            if not data:
                return jsonify({"error": "Invalid JSON data", "status": "error"}), 400
                
            user_message = data.get('message', '').strip()
            user_id = data.get('userId', '')
            academic_profile = data.get('academicProfile', '')
            
            # Create enhanced prompt with user context
            if academic_profile and academic_profile.strip():
                enhanced_prompt = f"User Profile: {academic_profile}\n\n{user_message}\n\nPlease provide helpful, specific advice for studying abroad. Format your response clearly without markdown symbols like ** or ##. Use bullet points with • symbol for lists."
            else:
                enhanced_prompt = f"{user_message}\n\nPlease provide helpful advice for studying abroad. Format your response clearly without markdown symbols like ** or ##. Use bullet points with • symbol for lists."
        else:
            # Form data request (backward compatibility)
            user_message = request.form.get('userMessage', '').strip()
            if not user_message:
                user_message = request.get_data(as_text=True).strip()
            enhanced_prompt = f"{user_message}\n\nPlease provide helpful advice for studying abroad. Format your response clearly without markdown symbols like ** or ##. Use bullet points with • symbol for lists."
            user_id = "form_user"
            academic_profile = ""

        # Validate user message
        if not user_message:
            error_msg = "Message cannot be empty"
            return jsonify({"error": error_msg, "status": "error"}), 400

        # Print received request
        print("\n" + "="*50)
        print("Received Request:")
        print(f"User Message: {user_message}")
        print(f"User ID: {user_id}")
        print(f"Academic Profile: {academic_profile}")
        print(f"Enhanced Prompt: {enhanced_prompt}")
        print("="*50)

        # Prepare payload for Ollama API
        payload = {
            "model": MODEL,
            "prompt": enhanced_prompt,
            "stream": False,
            "options": {
                "temperature": 0.7,
                "top_p": 0.9,
                "num_predict": 300
            }
        }

        # Send request to Ollama API
        try:
            print("Sending request to Ollama...")
            response = requests.post(OLLAMA_API_URL, json=payload, timeout=60)
            print(f"Ollama Response Status: {response.status_code}")
            
            if response.status_code != 200:
                print(f"Ollama Error Response: {response.text}")
                raise requests.RequestException(f"Ollama server returned status {response.status_code}")
            
            result = response.json()
            raw_output = result.get("response", "").strip()
            
            if not raw_output:
                print("Warning: Ollama returned empty response")
                raw_output = "I apologize, but I couldn't generate a response at the moment. Please try again."
                
        except requests.RequestException as e:
            print(f"Error during Ollama API call: {str(e)}")
            raw_output = f"I'm sorry, but I'm having trouble connecting to the AI service right now. Please try again later. Error: {str(e)}"
        except json.JSONDecodeError as e:
            print(f"Error parsing Ollama response: {str(e)}")
            raw_output = "I'm sorry, but I received an invalid response from the AI service. Please try again."
        except Exception as e:
            print(f"Unexpected error during Ollama API call: {str(e)}")
            raw_output = f"An unexpected error occurred. Please try again. Error: {str(e)}"

        # Use raw output as response
        ai_response = raw_output

        # Final validation
        if not ai_response or ai_response.isspace():
            ai_response = f"I apologize, but I couldn't provide a relevant answer to your question: '{user_message}'. Could you please rephrase your question?"

        # Print generated response
        print(f"\nGenerated AI Response: {ai_response}")
        print("="*50 + "\n")

        # Always return JSON response for consistency
        response_data = {
            "response": ai_response,
            "status": "success",
            "userId": user_id,
            "timestamp": int(time.time())  # Use simple timestamp
        }
        return jsonify(response_data)

    except Exception as e:
        error_msg = f"Server error: {str(e)}"
        print(f"Server Error: {error_msg}")
        
        return jsonify({"error": error_msg, "status": "error"}), 500

@app.errorhandler(404)
def not_found(error):
    return jsonify({"error": "Endpoint not found", "status": "error"}), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({"error": "Internal server error", "status": "error"}), 500

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', type=int, default=5000, help='Specify the port number')
    parser.add_argument('--host', type=str, default='0.0.0.0', help='Specify the host address')
    args = parser.parse_args()

    port_num = args.port
    host_addr = args.host
    
    print("="*60)
    print("StudyAbroad AI Backend Server")
    print("="*60)
    
    if check_ollama_server():
        print(f"✅ Server starting on {host_addr}:{port_num}")
        print(f"📱 Android emulator URL: http://10.0.2.2:{port_num}")
        print(f"🔗 Local test URL: http://localhost:{port_num}")
        print(f"🌐 Network URL: http://{host_addr}:{port_num}")
        print("⏹️  Press Ctrl+C to stop the server")
        print("="*60)
        
        app.run(host=host_addr, port=port_num, debug=True)
    else:
        print("❌ Exiting due to Ollama server unavailability.")
        print("Please start Ollama server with: ollama serve")
        print("Then run this script again.")