## Interface Contracts

### Skill Matching API

**Endpoint:** `POST /api/matches`  
**Content-Type:** `application/json`  
**Purpose:** Compares submitted employee skills with the prototype’s fictional opportunities and returns matching, missing, and unverified skills.

**Request**

```json
{
  "employeeSkills": [
    {"name": "Java", "verificationStatus": "VERIFIED"},
    {"name": "Git", "verificationStatus": "UNVERIFIED"}
  ]
}
