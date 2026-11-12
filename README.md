# GrowWithin AI

GrowWithin AI is an Alpha workforce development application that matches employee skills with internal career opportunities. It identifies verified, unverified, and missing skills and uses Google Gemini to generate a short career development recommendation.

## Alpha Release Features

- Spring Boot REST API
- Employee skill validation
- Skill-to-opportunity matching
- Readiness and skill-gap analysis
- Google Gemini AI career recommendations
- Safe fallback when the AI service is unavailable
- Automated unit and controller tests
- GitHub Actions continuous integration

## Technology Stack

- Java 21
- Spring Boot 4
- Maven
- Google Gemini API
- GitHub Actions

## Project Structure

- `backend` — Spring Boot API, matching logic, AI integration, and tests
- `.github/workflows` — automated CI configuration

## API Endpoints

### Skill Matching

`POST /api/matches`

Returns matching opportunities without an AI recommendation.

### Skill Matching with AI Advice

`POST /api/matches/with-advice`

Returns opportunity matches and a Gemini-generated career recommendation.

Example request:

```json
{
  "employeeSkills": [
    {
      "name": "Java",
      "verificationStatus": "VERIFIED"
    },
    {
      "name": "Git",
      "verificationStatus": "VERIFIED"
    },
    {
      "name": "REST APIs",
      "verificationStatus": "UNVERIFIED"
    }
  ]
}
```

## Local Setup

The Gemini API key must be stored in an environment variable. Do not add API keys to the repository.

```bash
export GEMINI_API_KEY='your-api-key'
cd backend
sh mvnw spring-boot:run
```

The application runs at `http://localhost:8080`.

The AI endpoint can be tested with:

```bash
curl -X POST http://localhost:8080/api/matches/with-advice \
  -H "Content-Type: application/json" \
  -d '{
    "employeeSkills": [
      {"name": "Java", "verificationStatus": "VERIFIED"},
      {"name": "Git", "verificationStatus": "VERIFIED"},
      {"name": "REST APIs", "verificationStatus": "UNVERIFIED"}
    ]
  }'
```

## Testing

Run all backend tests from the repository root:

```bash
sh backend/mvnw -f backend/pom.xml test
```

GitHub Actions also runs the backend tests automatically for feature branch pushes and pull requests to `main`.

## Security

API keys are read from environment variables and are not stored in source control. AI-generated recommendations must be reviewed by a person before they are used for career decisions.

## Team

- Anastasiia Korpusova — Integration Lead
- Janae Henry — Interface Designer
- Lead Architect responsibilities are shared

## Alpha Status

The Alpha release demonstrates integrated skill matching and functional AI-generated career advice. Additional user-interface, database, and deployment improvements are planned for future iterations.
