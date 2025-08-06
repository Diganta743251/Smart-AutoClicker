# 🔌 TapCraft Pro - API Documentation

## 🚀 **API Overview**

TapCraft Pro provides a comprehensive REST API that allows developers to integrate automation capabilities into their applications, manage scripts programmatically, and access analytics data.

## 📋 **Table of Contents**

1. [Authentication](#authentication)
2. [API Endpoints](#api-endpoints)
3. [Script Management](#script-management)
4. [Execution Control](#execution-control)
5. [Analytics & Reporting](#analytics--reporting)
6. [Team Collaboration](#team-collaboration)
7. [Integration Platform](#integration-platform)
8. [Enterprise APIs](#enterprise-apis)
9. [Webhooks](#webhooks)
10. [SDKs & Libraries](#sdks--libraries)

---

## 🔐 **Authentication**

### **API Key Authentication**

#### **Getting Your API Key**
1. Go to **Settings** → **Developer** in TapCraft Pro
2. Click **"Generate API Key"**
3. Choose permissions (Read, Write, Execute, Analytics)
4. Copy and securely store your API key

#### **Authentication Header**
```http
Authorization: Bearer YOUR_API_KEY
Content-Type: application/json
```

#### **API Key Permissions**
```json
{
  "permissions": {
    "scripts:read": "View script data",
    "scripts:write": "Create and modify scripts",
    "scripts:execute": "Execute scripts remotely",
    "analytics:read": "Access analytics data",
    "team:manage": "Manage team members",
    "integrations:manage": "Configure integrations"
  }
}
```

### **OAuth 2.0 Authentication**

#### **Authorization Flow**
```http
# Step 1: Authorization Request
GET https://api.tapcraft.pro/oauth/authorize
  ?client_id=YOUR_CLIENT_ID
  &response_type=code
  &scope=scripts:read scripts:write scripts:execute
  &redirect_uri=https://yourapp.com/callback
  &state=random_state_string

# Step 2: Token Exchange
POST https://api.tapcraft.pro/oauth/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=AUTHORIZATION_CODE
&client_id=YOUR_CLIENT_ID
&client_secret=YOUR_CLIENT_SECRET
&redirect_uri=https://yourapp.com/callback
```

#### **Token Response**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "def50200...",
  "scope": "scripts:read scripts:write scripts:execute"
}
```

---

## 🌐 **API Endpoints**

### **Base URLs**
- **Production**: `https://api.tapcraft.pro/v1`
- **Staging**: `https://api-staging.tapcraft.pro/v1`
- **Development**: `https://api-dev.tapcraft.pro/v1`

### **Rate Limits**
```json
{
  "rate_limits": {
    "free_tier": {
      "requests_per_minute": 60,
      "requests_per_hour": 1000,
      "requests_per_day": 10000
    },
    "pro_tier": {
      "requests_per_minute": 300,
      "requests_per_hour": 10000,
      "requests_per_day": 100000
    },
    "business_tier": {
      "requests_per_minute": 1000,
      "requests_per_hour": 50000,
      "requests_per_day": 1000000
    },
    "enterprise_tier": {
      "requests_per_minute": -1,
      "requests_per_hour": -1,
      "requests_per_day": -1
    }
  }
}
```

### **Response Format**
```json
{
  "success": true,
  "data": {
    // Response data
  },
  "meta": {
    "timestamp": "2025-01-27T10:00:00Z",
    "request_id": "req_123456789",
    "rate_limit": {
      "remaining": 59,
      "reset": 1643284800
    }
  }
}
```

### **Error Response Format**
```json
{
  "success": false,
  "error": {
    "code": "INVALID_REQUEST",
    "message": "The request is invalid",
    "details": "Script ID is required",
    "documentation_url": "https://docs.tapcraft.pro/errors#invalid-request"
  },
  "meta": {
    "timestamp": "2025-01-27T10:00:00Z",
    "request_id": "req_123456789"
  }
}
```

---

## 📝 **Script Management**

### **List Scripts**
```http
GET /scripts
```

#### **Parameters**
```json
{
  "page": 1,
  "limit": 20,
  "sort": "created_at",
  "order": "desc",
  "filter": {
    "status": "active",
    "category": "social_media",
    "tags": ["instagram", "automation"]
  }
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "scripts": [
      {
        "id": "script_123456",
        "name": "Instagram Auto-Like",
        "description": "Automatically like posts in Instagram feed",
        "category": "social_media",
        "tags": ["instagram", "like", "automation"],
        "status": "active",
        "created_at": "2025-01-27T10:00:00Z",
        "updated_at": "2025-01-27T10:30:00Z",
        "execution_count": 150,
        "success_rate": 95.5,
        "average_duration": 2.5
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 45,
      "pages": 3
    }
  }
}
```

### **Get Script Details**
```http
GET /scripts/{script_id}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "script": {
      "id": "script_123456",
      "name": "Instagram Auto-Like",
      "description": "Automatically like posts in Instagram feed",
      "category": "social_media",
      "tags": ["instagram", "like", "automation"],
      "status": "active",
      "nodes": [
        {
          "id": "node_1",
          "type": "app_launch",
          "config": {
            "package_name": "com.instagram.android",
            "wait_time": 3000
          },
          "position": {"x": 100, "y": 100}
        },
        {
          "id": "node_2",
          "type": "image_detection",
          "config": {
            "target_image": "heart_icon.png",
            "threshold": 0.8,
            "timeout": 5000
          },
          "position": {"x": 200, "y": 100}
        },
        {
          "id": "node_3",
          "type": "click",
          "config": {
            "target": "detected_element",
            "duration": 100
          },
          "position": {"x": 300, "y": 100}
        }
      ],
      "connections": [
        {"from": "node_1", "to": "node_2"},
        {"from": "node_2", "to": "node_3"}
      ],
      "variables": {
        "like_count": 0,
        "max_likes": 10
      },
      "settings": {
        "execution_timeout": 300000,
        "retry_on_failure": true,
        "max_retries": 3
      },
      "created_at": "2025-01-27T10:00:00Z",
      "updated_at": "2025-01-27T10:30:00Z"
    }
  }
}
```

### **Create Script**
```http
POST /scripts
```

#### **Request Body**
```json
{
  "name": "New Automation Script",
  "description": "Description of the automation",
  "category": "productivity",
  "tags": ["email", "automation"],
  "nodes": [
    {
      "id": "node_1",
      "type": "app_launch",
      "config": {
        "package_name": "com.google.android.gm",
        "wait_time": 3000
      },
      "position": {"x": 100, "y": 100}
    }
  ],
  "connections": [],
  "variables": {},
  "settings": {
    "execution_timeout": 300000,
    "retry_on_failure": true,
    "max_retries": 3
  }
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "script": {
      "id": "script_789012",
      "name": "New Automation Script",
      "status": "draft",
      "created_at": "2025-01-27T11:00:00Z"
    }
  }
}
```

### **Update Script**
```http
PUT /scripts/{script_id}
```

#### **Request Body**
```json
{
  "name": "Updated Script Name",
  "description": "Updated description",
  "nodes": [
    // Updated nodes array
  ],
  "connections": [
    // Updated connections array
  ]
}
```

### **Delete Script**
```http
DELETE /scripts/{script_id}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "message": "Script deleted successfully"
  }
}
```

---

## ▶️ **Execution Control**

### **Execute Script**
```http
POST /scripts/{script_id}/execute
```

#### **Request Body**
```json
{
  "parameters": {
    "username": "testuser",
    "iterations": 5,
    "delay_between_actions": 1000
  },
  "options": {
    "async": true,
    "timeout": 300000,
    "retry_on_failure": true,
    "notification_webhook": "https://yourapp.com/webhook"
  }
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "execution": {
      "id": "exec_123456789",
      "script_id": "script_123456",
      "status": "running",
      "started_at": "2025-01-27T12:00:00Z",
      "estimated_duration": 30,
      "progress": 0,
      "current_step": "Launching Instagram app"
    }
  }
}
```

### **Get Execution Status**
```http
GET /executions/{execution_id}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "execution": {
      "id": "exec_123456789",
      "script_id": "script_123456",
      "status": "completed",
      "started_at": "2025-01-27T12:00:00Z",
      "completed_at": "2025-01-27T12:02:30Z",
      "duration": 150,
      "progress": 100,
      "result": {
        "success": true,
        "actions_executed": 15,
        "actions_successful": 15,
        "actions_failed": 0,
        "data_collected": {
          "likes_given": 10,
          "posts_processed": 10
        }
      },
      "logs": [
        {
          "timestamp": "2025-01-27T12:00:05Z",
          "level": "info",
          "message": "Instagram app launched successfully"
        },
        {
          "timestamp": "2025-01-27T12:00:10Z",
          "level": "info",
          "message": "Heart icon detected at coordinates (500, 800)"
        }
      ]
    }
  }
}
```

### **Stop Execution**
```http
POST /executions/{execution_id}/stop
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "execution": {
      "id": "exec_123456789",
      "status": "stopped",
      "stopped_at": "2025-01-27T12:01:30Z",
      "reason": "User requested stop"
    }
  }
}
```

### **List Executions**
```http
GET /executions
```

#### **Parameters**
```json
{
  "script_id": "script_123456",
  "status": "completed",
  "start_date": "2025-01-20T00:00:00Z",
  "end_date": "2025-01-27T23:59:59Z",
  "page": 1,
  "limit": 20
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "executions": [
      {
        "id": "exec_123456789",
        "script_id": "script_123456",
        "script_name": "Instagram Auto-Like",
        "status": "completed",
        "started_at": "2025-01-27T12:00:00Z",
        "duration": 150,
        "success": true,
        "actions_executed": 15
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "pages": 5
    }
  }
}
```

---

## 📊 **Analytics & Reporting**

### **Get Script Analytics**
```http
GET /analytics/scripts/{script_id}
```

#### **Parameters**
```json
{
  "period": "7d",
  "metrics": ["executions", "success_rate", "duration", "errors"],
  "granularity": "daily"
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "analytics": {
      "script_id": "script_123456",
      "period": "7d",
      "summary": {
        "total_executions": 150,
        "successful_executions": 143,
        "failed_executions": 7,
        "success_rate": 95.33,
        "average_duration": 145.5,
        "total_duration": 21825
      },
      "metrics": {
        "executions": [
          {"date": "2025-01-21", "value": 20},
          {"date": "2025-01-22", "value": 25},
          {"date": "2025-01-23", "value": 18},
          {"date": "2025-01-24", "value": 22},
          {"date": "2025-01-25", "value": 28},
          {"date": "2025-01-26", "value": 19},
          {"date": "2025-01-27", "value": 18}
        ],
        "success_rate": [
          {"date": "2025-01-21", "value": 95.0},
          {"date": "2025-01-22", "value": 96.0},
          {"date": "2025-01-23", "value": 94.4},
          {"date": "2025-01-24", "value": 95.5},
          {"date": "2025-01-25", "value": 96.4},
          {"date": "2025-01-26", "value": 94.7},
          {"date": "2025-01-27", "value": 94.4}
        ]
      },
      "errors": [
        {
          "type": "detection_timeout",
          "count": 4,
          "percentage": 57.1
        },
        {
          "type": "app_not_found",
          "count": 2,
          "percentage": 28.6
        },
        {
          "type": "network_error",
          "count": 1,
          "percentage": 14.3
        }
      ]
    }
  }
}
```

### **Get User Analytics**
```http
GET /analytics/user
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "analytics": {
      "user_id": "user_123456",
      "period": "30d",
      "summary": {
        "total_scripts": 25,
        "active_scripts": 18,
        "total_executions": 1250,
        "successful_executions": 1188,
        "time_saved_hours": 42.5,
        "automation_efficiency": 94.8
      },
      "usage_patterns": {
        "most_active_day": "Tuesday",
        "most_active_hour": 14,
        "average_daily_executions": 41.7,
        "peak_usage_time": "14:00-15:00"
      },
      "top_scripts": [
        {
          "script_id": "script_123456",
          "name": "Instagram Auto-Like",
          "executions": 150,
          "success_rate": 95.33
        },
        {
          "script_id": "script_789012",
          "name": "Email Automation",
          "executions": 120,
          "success_rate": 98.33
        }
      ]
    }
  }
}
```

### **Generate Report**
```http
POST /analytics/reports
```

#### **Request Body**
```json
{
  "type": "performance_report",
  "period": "30d",
  "filters": {
    "script_ids": ["script_123456", "script_789012"],
    "categories": ["social_media", "productivity"]
  },
  "format": "pdf",
  "email_to": "user@example.com"
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "report": {
      "id": "report_123456",
      "type": "performance_report",
      "status": "generating",
      "download_url": null,
      "estimated_completion": "2025-01-27T12:05:00Z"
    }
  }
}
```

---

## 👥 **Team Collaboration**

### **List Team Members**
```http
GET /team/members
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "members": [
      {
        "id": "user_123456",
        "email": "john.doe@company.com",
        "name": "John Doe",
        "role": "admin",
        "status": "active",
        "last_active": "2025-01-27T11:30:00Z",
        "joined_at": "2025-01-15T09:00:00Z"
      },
      {
        "id": "user_789012",
        "email": "jane.smith@company.com",
        "name": "Jane Smith",
        "role": "editor",
        "status": "active",
        "last_active": "2025-01-27T10:45:00Z",
        "joined_at": "2025-01-16T14:30:00Z"
      }
    ]
  }
}
```

### **Invite Team Member**
```http
POST /team/invitations
```

#### **Request Body**
```json
{
  "email": "newuser@company.com",
  "role": "editor",
  "message": "Welcome to our automation team!"
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "invitation": {
      "id": "inv_123456",
      "email": "newuser@company.com",
      "role": "editor",
      "status": "pending",
      "expires_at": "2025-02-03T12:00:00Z",
      "invited_by": "user_123456"
    }
  }
}
```

### **Share Script**
```http
POST /scripts/{script_id}/share
```

#### **Request Body**
```json
{
  "user_ids": ["user_789012", "user_345678"],
  "permission": "edit",
  "message": "Please review this automation script"
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "shares": [
      {
        "user_id": "user_789012",
        "permission": "edit",
        "shared_at": "2025-01-27T12:00:00Z"
      },
      {
        "user_id": "user_345678",
        "permission": "edit",
        "shared_at": "2025-01-27T12:00:00Z"
      }
    ]
  }
}
```

---

## 🔗 **Integration Platform**

### **List Available Integrations**
```http
GET /integrations
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "integrations": [
      {
        "id": "slack",
        "name": "Slack",
        "description": "Team communication platform",
        "category": "communication",
        "icon": "https://cdn.tapcraft.pro/icons/slack.png",
        "status": "available",
        "auth_type": "oauth2",
        "capabilities": ["send_message", "create_channel", "invite_user"],
        "pricing": "free"
      },
      {
        "id": "salesforce",
        "name": "Salesforce",
        "description": "Customer relationship management",
        "category": "crm",
        "icon": "https://cdn.tapcraft.pro/icons/salesforce.png",
        "status": "available",
        "auth_type": "oauth2",
        "capabilities": ["create_lead", "update_contact", "query_data"],
        "pricing": "premium"
      }
    ]
  }
}
```

### **Connect Integration**
```http
POST /integrations/{integration_id}/connect
```

#### **Request Body**
```json
{
  "name": "My Slack Workspace",
  "configuration": {
    "workspace_url": "https://mycompany.slack.com",
    "default_channel": "#automation"
  },
  "auth_data": {
    "access_token": "xoxb-your-slack-token",
    "team_id": "T1234567890"
  }
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "connection": {
      "id": "conn_123456",
      "integration_id": "slack",
      "name": "My Slack Workspace",
      "status": "connected",
      "connected_at": "2025-01-27T12:00:00Z",
      "last_used": null
    }
  }
}
```

### **Execute Integration Action**
```http
POST /integrations/{connection_id}/actions/{action_id}
```

#### **Request Body**
```json
{
  "parameters": {
    "channel": "#general",
    "text": "Automation script completed successfully!",
    "username": "TapCraft Bot"
  }
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "result": {
      "action_id": "send_message",
      "status": "success",
      "response": {
        "message_id": "1234567890.123456",
        "timestamp": "2025-01-27T12:00:00Z"
      }
    }
  }
}
```

---

## 🏢 **Enterprise APIs**

### **User Management**
```http
GET /enterprise/users
POST /enterprise/users
PUT /enterprise/users/{user_id}
DELETE /enterprise/users/{user_id}
```

### **License Management**
```http
GET /enterprise/license
PUT /enterprise/license
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "license": {
      "type": "enterprise",
      "seats_total": 1000,
      "seats_used": 245,
      "features": [
        "unlimited_scripts",
        "advanced_analytics",
        "sso_integration",
        "audit_logging",
        "priority_support"
      ],
      "expires_at": "2026-01-27T00:00:00Z",
      "support_level": "premium"
    }
  }
}
```

### **Audit Logs**
```http
GET /enterprise/audit-logs
```

#### **Parameters**
```json
{
  "start_date": "2025-01-20T00:00:00Z",
  "end_date": "2025-01-27T23:59:59Z",
  "user_id": "user_123456",
  "action": "script_executed",
  "resource": "script_123456"
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "logs": [
      {
        "id": "log_123456",
        "timestamp": "2025-01-27T12:00:00Z",
        "user_id": "user_123456",
        "user_email": "john.doe@company.com",
        "action": "script_executed",
        "resource": "script_123456",
        "resource_name": "Instagram Auto-Like",
        "ip_address": "192.168.1.100",
        "user_agent": "TapCraft Pro/1.0.0",
        "details": {
          "execution_id": "exec_123456789",
          "duration": 150,
          "success": true
        }
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 50,
      "total": 1250,
      "pages": 25
    }
  }
}
```

---

## 🔔 **Webhooks**

### **Create Webhook**
```http
POST /webhooks
```

#### **Request Body**
```json
{
  "url": "https://yourapp.com/webhook",
  "events": [
    "script.executed",
    "script.failed",
    "user.created",
    "integration.connected"
  ],
  "secret": "your_webhook_secret",
  "active": true
}
```

#### **Response**
```json
{
  "success": true,
  "data": {
    "webhook": {
      "id": "webhook_123456",
      "url": "https://yourapp.com/webhook",
      "events": [
        "script.executed",
        "script.failed",
        "user.created",
        "integration.connected"
      ],
      "secret": "your_webhook_secret",
      "active": true,
      "created_at": "2025-01-27T12:00:00Z"
    }
  }
}
```

### **Webhook Payload Example**
```json
{
  "event": "script.executed",
  "timestamp": "2025-01-27T12:00:00Z",
  "data": {
    "execution_id": "exec_123456789",
    "script_id": "script_123456",
    "script_name": "Instagram Auto-Like",
    "user_id": "user_123456",
    "status": "completed",
    "duration": 150,
    "success": true,
    "actions_executed": 15,
    "result": {
      "likes_given": 10,
      "posts_processed": 10
    }
  },
  "signature": "sha256=abc123def456..."
}
```

### **Webhook Signature Verification**
```python
import hmac
import hashlib

def verify_webhook_signature(payload, signature, secret):
    expected_signature = hmac.new(
        secret.encode('utf-8'),
        payload.encode('utf-8'),
        hashlib.sha256
    ).hexdigest()
    
    return hmac.compare_digest(
        f"sha256={expected_signature}",
        signature
    )
```

---

## 📚 **SDKs & Libraries**

### **JavaScript/Node.js SDK**
```bash
npm install tapcraft-pro-sdk
```

```javascript
const TapCraft = require('tapcraft-pro-sdk');

const client = new TapCraft({
  apiKey: 'your_api_key',
  baseUrl: 'https://api.tapcraft.pro/v1'
});

// Execute a script
const execution = await client.scripts.execute('script_123456', {
  parameters: {
    username: 'testuser',
    iterations: 5
  }
});

console.log('Execution ID:', execution.id);
console.log('Status:', execution.status);

// Get execution status
const status = await client.executions.get(execution.id);
console.log('Current status:', status.status);
console.log('Progress:', status.progress);
```

### **Python SDK**
```bash
pip install tapcraft-pro-sdk
```

```python
from tapcraft_pro import TapCraftClient

client = TapCraftClient(
    api_key='your_api_key',
    base_url='https://api.tapcraft.pro/v1'
)

# List scripts
scripts = client.scripts.list(
    category='social_media',
    limit=10
)

for script in scripts:
    print(f"Script: {script.name} ({script.id})")

# Execute script
execution = client.scripts.execute(
    script_id='script_123456',
    parameters={
        'username': 'testuser',
        'iterations': 5
    }
)

print(f"Execution started: {execution.id}")

# Wait for completion
result = client.executions.wait_for_completion(
    execution.id,
    timeout=300
)

print(f"Execution completed: {result.success}")
print(f"Duration: {result.duration} seconds")
```

### **Java SDK**
```xml
<dependency>
    <groupId>com.tapcraft.pro</groupId>
    <artifactId>tapcraft-pro-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
import com.tapcraft.pro.TapCraftClient;
import com.tapcraft.pro.models.Script;
import com.tapcraft.pro.models.Execution;

TapCraftClient client = new TapCraftClient.Builder()
    .apiKey("your_api_key")
    .baseUrl("https://api.tapcraft.pro/v1")
    .build();

// Get script details
Script script = client.scripts().get("script_123456");
System.out.println("Script: " + script.getName());

// Execute script
Map<String, Object> parameters = new HashMap<>();
parameters.put("username", "testuser");
parameters.put("iterations", 5);

Execution execution = client.scripts().execute("script_123456", parameters);
System.out.println("Execution ID: " + execution.getId());

// Monitor execution
ExecutionStatus status = client.executions().getStatus(execution.getId());
while (status.isRunning()) {
    Thread.sleep(5000);
    status = client.executions().getStatus(execution.getId());
    System.out.println("Progress: " + status.getProgress() + "%");
}

System.out.println("Execution completed: " + status.isSuccess());
```

### **cURL Examples**

#### **Execute Script**
```bash
curl -X POST "https://api.tapcraft.pro/v1/scripts/script_123456/execute" \
  -H "Authorization: Bearer YOUR_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "parameters": {
      "username": "testuser",
      "iterations": 5
    },
    "options": {
      "async": true,
      "timeout": 300000
    }
  }'
```

#### **Get Execution Status**
```bash
curl -X GET "https://api.tapcraft.pro/v1/executions/exec_123456789" \
  -H "Authorization: Bearer YOUR_API_KEY"
```

#### **List Scripts**
```bash
curl -X GET "https://api.tapcraft.pro/v1/scripts?category=social_media&limit=10" \
  -H "Authorization: Bearer YOUR_API_KEY"
```

---

## 🔧 **Error Handling**

### **Error Codes**
```json
{
  "error_codes": {
    "INVALID_REQUEST": {
      "code": 400,
      "description": "The request is malformed or missing required parameters"
    },
    "UNAUTHORIZED": {
      "code": 401,
      "description": "Invalid or missing API key"
    },
    "FORBIDDEN": {
      "code": 403,
      "description": "Insufficient permissions for this operation"
    },
    "NOT_FOUND": {
      "code": 404,
      "description": "The requested resource was not found"
    },
    "RATE_LIMITED": {
      "code": 429,
      "description": "Rate limit exceeded"
    },
    "INTERNAL_ERROR": {
      "code": 500,
      "description": "An internal server error occurred"
    },
    "SCRIPT_EXECUTION_FAILED": {
      "code": 422,
      "description": "Script execution failed due to automation error"
    },
    "QUOTA_EXCEEDED": {
      "code": 402,
      "description": "Usage quota exceeded, upgrade required"
    }
  }
}
```

### **Retry Logic**
```javascript
async function executeWithRetry(scriptId, parameters, maxRetries = 3) {
  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      const result = await client.scripts.execute(scriptId, parameters);
      return result;
    } catch (error) {
      if (error.code === 'RATE_LIMITED') {
        const retryAfter = error.headers['retry-after'] || Math.pow(2, attempt);
        await sleep(retryAfter * 1000);
        continue;
      }
      
      if (attempt === maxRetries) {
        throw error;
      }
      
      await sleep(1000 * attempt); // Exponential backoff
    }
  }
}
```

---

## 📈 **Best Practices**

### **API Usage Guidelines**

1. **Authentication**
   - Store API keys securely
   - Use environment variables
   - Rotate keys regularly
   - Implement proper error handling

2. **Rate Limiting**
   - Respect rate limits
   - Implement exponential backoff
   - Cache responses when possible
   - Use webhooks for real-time updates

3. **Error Handling**
   - Handle all error codes gracefully
   - Implement retry logic for transient errors
   - Log errors for debugging
   - Provide meaningful error messages to users

4. **Performance**
   - Use pagination for large datasets
   - Implement request caching
   - Minimize API calls
   - Use batch operations when available

5. **Security**
   - Validate webhook signatures
   - Use HTTPS for all requests
   - Implement proper access controls
   - Monitor API usage for anomalies

---

*For more detailed API documentation and interactive examples, visit our developer portal at docs.tapcraft.pro*