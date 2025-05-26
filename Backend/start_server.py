#!/usr/bin/env python3
"""
Startup script for the StudyAbroad AI Backend Server
"""

import subprocess
import sys
import os
import time
import requests

def check_python_version():
    """Check if Python version is compatible"""
    if sys.version_info < (3, 7):
        print("❌ Error: Python 3.7 or higher is required")
        print(f"Current version: {sys.version}")
        return False
    print(f"✅ Python version: {sys.version}")
    return True

def check_ollama_server():
    """Check if Ollama server is running"""
    try:
        response = requests.get("http://localhost:11434", timeout=5)
        if response.status_code == 200:
            print("✅ Ollama server is running")
            return True
        else:
            print("❌ Ollama server is not responding correctly")
            return False
    except requests.ConnectionError:
        print("❌ Ollama server is not running")
        print("Please start Ollama server with: ollama serve")
        return False
    except Exception as e:
        print(f"❌ Error checking Ollama server: {e}")
        return False

def install_requirements():
    """Install required packages"""
    print("📦 Installing requirements...")
    try:
        subprocess.check_call([sys.executable, "-m", "pip", "install", "-r", "requirements.txt"])
        print("✅ Requirements installed successfully")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ Error installing requirements: {e}")
        return False

def check_model():
    """Check if the required model is available"""
    try:
        result = subprocess.run(["ollama", "list"], capture_output=True, text=True)
        if "llama3.2" in result.stdout:
            print("✅ Llama3.2 model is available")
            return True
        else:
            print("❌ Llama3.2 model not found")
            print("Please install it with: ollama pull llama3.2:latest")
            return False
    except FileNotFoundError:
        print("❌ Ollama command not found. Please install Ollama first.")
        return False
    except Exception as e:
        print(f"❌ Error checking model: {e}")
        return False

def start_server():
    """Start the Flask server"""
    print("🚀 Starting Flask server...")
    try:
        # Change to the Backend directory
        os.chdir(os.path.dirname(os.path.abspath(__file__)))
        
        # Start the server
        subprocess.run([sys.executable, "main-ollama.py", "--port", "5000"])
    except KeyboardInterrupt:
        print("\n🛑 Server stopped by user")
    except Exception as e:
        print(f"❌ Error starting server: {e}")

def main():
    print("=" * 60)
    print("🎓 StudyAbroad AI Backend Server Startup")
    print("=" * 60)
    
    # Check Python version
    if not check_python_version():
        return
    
    # Install requirements
    if not install_requirements():
        return
    
    # Check Ollama server
    if not check_ollama_server():
        print("\n📝 To start Ollama server:")
        print("1. Open a new terminal")
        print("2. Run: ollama serve")
        print("3. Keep that terminal open")
        print("4. Run this script again")
        return
    
    # Check model
    if not check_model():
        print("\n📝 To install the model:")
        print("1. Run: ollama pull llama3.2:latest")
        print("2. Wait for download to complete")
        print("3. Run this script again")
        return
    
    print("\n✅ All checks passed! Starting server...")
    print("📱 Android app should connect to: http://10.0.2.2:5000 (emulator) or http://YOUR_IP:5000 (device)")
    print("🔗 Test URL: http://localhost:5000")
    print("⏹️  Press Ctrl+C to stop the server")
    print("-" * 60)
    
    # Start the server
    start_server()

if __name__ == "__main__":
    main() 