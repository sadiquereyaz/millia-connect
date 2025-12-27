## Step 1: Save the Python Script

Create a file called `captive_portal.py`:

```python
from http.server import HTTPServer, BaseHTTPRequestHandler

class CaptivePortalHandler(BaseHTTPRequestHandler):
    def do_GET(self):
if self.path == '/login':
# Serve a fake login page
self.send_response(200)
self.send_header('Content-type', 'text/html')
self.end_headers()
html = '''
<html>
<body>
<h1>Captive Portal Login</h1>
<p>Please sign in to use this network</p>
<form>
<input type="text" placeholder="Username">
<input type="password" placeholder="Password">
<button>Sign In</button>
</form>
</body>
</html>
'''
self.wfile.write(html.encode())
else:
# Redirect everything else to login page
self.send_response(302)
self.send_header('Location', 'http://192.168.1.100:8000/login')
self.end_headers()

def do_HEAD(self):
# Android uses HEAD requests to check connectivity
self.send_response(302)
self.send_header('Location', 'http://192.168.1.100:8000/login')
self.end_headers()

def log_message(self, format, *args):
# Print requests for debugging
print(f"{self.address_string()} - {format % args}")

if __name__ == '__main__':
server = HTTPServer(('0.0.0.0', 8000), CaptivePortalHandler)
print('Captive portal server running on port 8000...')
print('Press Ctrl+C to stop')
server.serve_forever()
```

## Step 2: Find Your Computer's IP Address

**Windows:**

```bash
ipconfig
# Look for "IPv4 Address" under your active network adapter
```

**Mac/Linux:**

```bash
ifconfig
# or
lear
# Look for inet address (e.g., 192.168.1.100)
```

**Quick way (all platforms):**

```bash
# Windows
ipconfig | findstr IPv4

# Mac/Linux
ifconfig | grep "inet "
```

## Step 3: Update the Script with Your IP

Replace `192.168.1.100` in the script with your actual computer's IP address.

## Step 4: Run the Server

**Option A: Direct Python (Python 3 required)**

```bash
# Navigate to the script directory
cd /path/to/script

# Run the server
python captive_portal.py

# or on some systems
python3 captive_portal.py
```

**Option B: Make it executable (Mac/Linux)**

```bash
    chmod +x captive_portal.py
    ./captive_portal.py
```

You should see:

```
Captive portal server running on port 8000...
Press Ctrl+C to stop
```

## Step 5: Configure Your Android Device

**On your Android device/emulator:**

1. Go to **Settings → Network & Internet → Wi-Fi**
2. **Long press** your connected Wi-Fi network
3. Tap **Modify network** or **Advanced**
4. Set **Proxy** to **Manual**
5. Enter:

- **Proxy hostname**: Your computer's IP (e.g., `192.168.1.100`)
- **Proxy port**: `8000`

6. Save

## Step 6: Test

```bash
# From your Android device, any HTTP request should redirect
# Try opening a browser on the device - you should see the login page
```

## Verify It's Working

You should see requests in your terminal:

```
192.168.1.50 - "GET /generate_204 HTTP/1.1" 302 -
192.168.1.50 - "GET /login HTTP/1.1" 200 -
```

## Troubleshooting

**Server won't start:**

```bash
# Port 8000 might be in use, try different port
python captive_portal.py
# Change port in script: HTTPServer(('0.0.0.0', 8080), ...)
```

**Android can't connect:**

```bash
# Check firewall - allow port 8000
# Windows Firewall: Allow Python through firewall
# Mac: System Preferences → Security & Privacy → Firewall
# Linux: sudo ufw allow 8000
```

**Test server is reachable:**

```bash
# From another device on same network
curl http://YOUR_COMPUTER_IP:8000
```

## Stop the Server

Press **Ctrl+C** in the terminal where it's running.

That's it! Now your Android device will think it's on a captive portal network when making HTTP
requests.