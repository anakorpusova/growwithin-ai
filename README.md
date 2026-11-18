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
- React interface for entering employee skills and viewing results
- Responsive layout with loading, validation, and error messages

## Technology Stack

- Java 21
- Spring Boot 4
- Maven
- Google Gemini API
- GitHub Actions
- React
- Vite
- Node.js 24 LTS and npm
- ESLint

## Project Structure

- `backend` - Spring Boot API, matching logic, AI integration, and tests
- `.github/workflows` - automated CI configuration
- `frontend` - React interface, styling, and Vite development configuration

## API Endpoints

### Skill Matching

`POST /api/matches`

Returns matching opportunities without an AI recommendation.

### Skill Matching with AI Advice

`POST /api/matches/with-advice`

Returns opportunity matches and a Gemini-generated career recommendation when available. If AI advice is unavailable, the response includes fallback information and preserves the skill matches.

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

The commands below use Bash or Git Bash. The backend requires JDK 21. The frontend requires Node.js 24 LTS and npm.

The Gemini API key must be stored in an environment variable. Do not add API keys to the repository.

From the repository root:

```bash
export GEMINI_API_KEY='your-api-key'
cd backend
sh mvnw spring-boot:run
```

Replace `your-api-key` with your actual key locally. Without `GEMINI_API_KEY`, skill matching remains available, but AI advice returns a fallback.

The backend runs at http://localhost:8080.

The AI endpoint can be tested from a separate terminal with:

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

## Run the Frontend

Start the backend using the instructions above. In a separate terminal, from the repository root:

```bash
cd frontend
npm ci
npm run dev
```

Open the URL printed by Vite, normally http://localhost:5173.

The development proxy forwards `/api` requests to the backend at http://localhost:8080. Keep both servers running.

Enter sample skills, select their verification status, and choose Find opportunities. Results show opportunity readiness, matching skills, missing skills, unverified skills, and AI advice when available.

When AI advice is unavailable, the interface displays a fallback message and preserves the skill matches.

For request and response formats, validation behavior, and frontend limitations, see [Frontend documentation](frontend/README.md).

## Testing

Run all backend tests from the repository root:

```bash
sh backend/mvnw -f backend/pom.xml test
```

GitHub Actions also runs the backend tests automatically for feature branch pushes and pull requests to `main`.

Run the frontend checks from the repository root:

```bash
cd frontend
npm run build
npm run lint
```

These frontend checks currently run locally. The existing GitHub Actions workflow runs backend tests.

## Security

API keys are read from environment variables and are not stored in source control. Never place the Gemini API key in frontend code.

Use fictional employee data for the Alpha demonstration. AI-generated recommendations must be reviewed by a person before they are used for career decisions.

## Team

- Anastasiia Korpusova - Integration Lead
- Janae Henry - Interface Designer
- Lead Architect responsibilities are shared

## Alpha Status

The Alpha includes a React interface connected to the Spring Boot skill-matching and Gemini advice endpoint. Local frontend testing confirmed that skill matches remain visible when AI advice is unavailable. Live Gemini advice was successfully verified through the React frontend using sample employee skills.

Current limitations include sample opportunities, manually selected skill verification status, no saved employee profiles, and no structured 30/60/90-day development plan. Production hosting and API routing remain future work.

During frontend testing, npm audit identified five high-severity dependency vulnerabilities related to Vite and picomatch. These findings are documented as technical debt and will be addressed in a future update.
