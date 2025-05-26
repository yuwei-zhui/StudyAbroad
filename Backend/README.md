# Backend Code For Task 8.1C 
For frontend sample android app: https://github.com/dgdeakin/Task8.1CAndroidAppExample

# Files
Three files are present, use the file that works for you (Feel free to edit).
1. main-directModel.py
2. main-pipeline.py
3. main-ollama.py (Need to download ollama, may skip steps 4, 5)

You can explore code from previous year from here:
https://github.com/sit3057082025/T-8.1C. 
Readme file is useful.

# Instructions:
- Almost similar to previous setups as in Task6.1D Backend code setup (For files 1 and 2).
- Detailed steps are in ReadME of this repo:
https://github.com/sit3057082025/BackendApiLLM_T6.1D
- For File 3, you need to install ollama.

## 1. First clone the repo or download to your local folder.
## 2. Run terminal in the project folder.
   - Make sure python is installed. https://www.python.org/downloads/
   - Create Virtual environment using this in terminal: ```python -m venv venv```
   - venv folder will be created.
   - now activate virtual environment
     - For MAC:
        ```source venv/bin/activate```
     - For Windows PS:
       ```.\venv\Scripts\Activate.ps1```
## 3. Now install the libraries
   - ```pip install Flask```
     (source: https://flask.palletsprojects.com/en/stable/installation/)
   - ```pip install accelerate```
   - ```pip install requests```
   - ```pip install transformers```
     (source: https://pypi.org/project/transformers/)
   - ```pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu126```
     (source: https://pytorch.org/get-started/locally/)
## 4. Settings with HuggingFace (Skip this if using main-ollama.py and go to Step 6 below)
   - Signup for Huggingface Account here: https://huggingface.co/
   - Create access token from here: https://huggingface.co/settings/tokens
   - Get Model "google/gemma-3-1b-it" from https://huggingface.co/google/gemma-3-1b-it.
      Acknowledge licence.

   <img src="screenshots/acknowledge_licence.png" width="500" alt="Description">

   - Then run this in terminal:
     ```huggingface-cli login```

   - It will ask for hugging face token, provide and press enter. Then select Y.
     
## 5. To run the code in terminal (Skip this if using main-ollama.py and go to stop 6 below): 
```python main-directModel.py```

or 

```python main-pipeline.py```


# 6. Using python main-ollama.py
a) First download and install ollama from: https://ollama.com/download

b) In your new terminal, run:
   - ```ollama serve```
   - You should get:
   
   <img src="screenshots/img_ollama.png" width="700">
   
NOTE: You might need to pull model as: ```ollama pull llama3.2:latest```

c) Then, in your backend (this project) terminal (in new from above 6.b terminal), run
   ```python main-ollama.py```
   
d) Then, either test the route points in postman as below (Steps 7a, 7b).

e) Or run the android app.

f) On successful run you should get:

  - in Ollama serve terminal

<img src="screenshots/img.png" width="800">
   


   - in your backend terminal,

<img src="screenshots/img_1.png" width="800">


   
   - in you android app,

<img src="screenshots/img_2.png" width="200">


# 7. To retrieve check response:
## a) Using curl in terminal PS (as shown in image below):
- PS D:\> curl.exe -X POST http://localhost:5000/chat -d "userMessage=Where is Australia?"
  
<img src="screenshots/curl.png" alt="Android App Screenshot" width="700">

## b) using PostMan (as shown in image below)
METHOD: POST

URL: http://localhost:5000/chat

Body: x-www-form-urlencoded
- key: userMessage
- Value: Where is Australia?


<img src="screenshots/postman.png" alt="Android App Screenshot" width="400">


# c) In Sample Android app:
- Use JsonRequest or other tools:

<img src="screenshots/response_directmodel.png" alt="Android App Screenshot" width="200">

<img src="screenshots/response_pipeline.png" alt="Android App Screenshot" width="200">

# 8. Troubleshooting
1. port 5000 may not be available sometimes, use different post like 5001. Change in android app as well the port number.
2. Make sure to run  "huggingface-cli login" command in terminal and provide token.
3. Make sure to acknowledge licence in the model site such as https://huggingface.co/google/gemma-3-1b-it
4. Select most permissions while creating access token.
5. Try with different models based on quick access. https://huggingface.co/models
6. Use small models.

# 9. Good to know about:
1. Python https://www.python.org/
2. Virtual Environments https://docs.python.org/3/library/venv.html
3. Flask https://flask.palletsprojects.com/en/stable/
4. Huggingface Transformers https://huggingface.co/docs/transformers/en/index

Further references (From Previous)
[https://github.com/sit3057082025/T-8.1C](https://github.com/sit3057082025/T-8.1C)


# References:
1. Transformers: State-of-the-art Machine Learning for Pytorch, TensorFlow, and JAX.
https://github.com/huggingface/transformers
2. A simple way to launch, train, and use PyTorch models on almost any device and distributed configuration, automatic mixed precision (including fp8), and easy-to-configure FSDP and DeepSpeed support
https://github.com/huggingface/accelerate
3. Ollama
https://ollama.com/
4. How to Build a Flask Python Web Application from Scratch
https://www.digitalocean.com/community/tutorials/how-to-make-a-web-application-using-flask-in-python-3
5. https://flask.palletsprojects.com/en/stable/
6. What is Postman?
https://www.postman.com/product/what-is-postman/
https://learning.postman.com/docs/getting-started/overview/
7. What is curl command? 
https://developer.ibm.com/articles/what-is-curl-command/
8. JSONObject
https://developer.android.com/reference/org/json/JSONObject
https://www.digitalocean.com/community/tutorials/android-jsonobject-json-parsing
9. Get up and running with Llama 3.3, DeepSeek-R1, Phi-4, Gemma 3, Mistral Small 3.1 and other large language models.
https://github.com/ollama/ollama
10. What are large language models?
https://www.cloudflare.com/en-gb/learning/ai/what-is-large-language-model/
https://www.ibm.com/think/topics/large-language-models
https://aws.amazon.com/what-is/large-language-model/
11. venv — Creation of virtual environments
https://docs.python.org/3/library/venv.html
12. What's the Difference Between Frontend and Backend in Application Development?
https://aws.amazon.com/compare/the-difference-between-frontend-and-backend/


# StudyAbroad AI Backend

这是StudyAbroad应用的AI后端服务，使用Ollama提供本地AI聊天功能。

## 功能特性

- 🤖 基于Ollama的本地AI聊天
- 📱 支持Android应用的JSON API
- 👤 用户个人资料集成
- 🎓 留学相关的智能建议
- 🔄 向后兼容的表单数据支持

## 系统要求

- Python 3.7+
- Ollama (用于AI模型)
- 至少4GB RAM (推荐8GB+)

## 安装步骤

### 1. 安装Ollama

访问 [Ollama官网](https://ollama.ai) 下载并安装Ollama。

### 2. 下载AI模型

```bash
ollama pull llama3.2:latest
```

### 3. 启动Ollama服务器

```bash
ollama serve
```

保持这个终端窗口打开。

### 4. 安装Python依赖

```bash
cd Backend
pip install -r requirements.txt
```

### 5. 启动Flask服务器

#### 方法1: 使用启动脚本 (推荐)
```bash
python start_server.py
```

#### 方法2: 直接启动
```bash
python main-ollama.py
```

## API文档

### 聊天端点

**URL:** `POST /chat`

**请求格式 (JSON):**
```json
{
    "message": "用户消息",
    "userId": "用户ID",
    "academicProfile": "用户学术背景信息"
}
```

**响应格式:**
```json
{
    "response": "AI回复",
    "status": "success"
}
```

**错误响应:**
```json
{
    "error": "错误信息",
    "status": "error"
}
```

### 向后兼容

服务器也支持表单数据请求：
```bash
curl -X POST http://localhost:5000/chat \
  -d "userMessage=Hello, can you help me with studying abroad?"
```

## Android应用配置

### 网络配置

在Android应用中，确保使用正确的服务器地址：

- **Android模拟器**: `http://10.0.2.2:5000/`
- **物理设备**: `http://YOUR_COMPUTER_IP:5000/`

### 修改IP地址

如果使用物理设备测试，需要修改 `ApiConfig.java` 中的 `BASE_URL_DEVICE`：

```java
public static final String BASE_URL_DEVICE = "http://192.168.1.100:5000/"; // 替换为你的IP
```

## 测试

### 测试服务器

```bash
python test_server.py
```

### 手动测试

```bash
curl -X POST http://localhost:5000/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What are the best universities for computer science?",
    "userId": "test_user",
    "academicProfile": "Major: Computer Science\nGPA: 3.8\nTarget: Master degree"
  }'
```

## 故障排除

### 常见问题

1. **连接错误**
   - 确保Ollama服务器正在运行 (`ollama serve`)
   - 检查防火墙设置
   - 验证端口5000没有被占用

2. **模型未找到**
   ```bash
   ollama pull llama3.2:latest
   ```

3. **Android连接失败**
   - 模拟器: 使用 `10.0.2.2:5000`
   - 物理设备: 确保电脑和手机在同一网络，使用电脑的IP地址

4. **权限错误**
   - 确保Android应用有网络权限
   - 检查 `usesCleartextTraffic="true"` 设置

### 日志查看

服务器会在控制台输出详细的请求和响应日志，包括：
- 接收到的请求
- Ollama API调用
- 生成的响应

## 开发说明

### 项目结构

```
Backend/
├── main-ollama.py      # 主服务器文件
├── start_server.py     # 启动脚本
├── test_server.py      # 测试脚本
├── requirements.txt    # Python依赖
└── README.md          # 说明文档
```

### 自定义配置

可以在 `main-ollama.py` 中修改：
- AI模型 (`MODEL` 变量)
- 生成参数 (temperature, top_p等)
- 端口号 (默认5000)

### 扩展功能

要添加新功能，可以：
1. 在Flask应用中添加新的路由
2. 修改Android应用的API接口
3. 更新数据模型

## 许可证

本项目仅供学习和研究使用。



