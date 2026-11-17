# GrowWithin AI Frontend

React interface for the GrowWithin AI Alpha release.

## Requirements

- Node.js 24 LTS and npm
- Backend running at http://localhost:8080
- JDK 21 to run the backend

## Run Locally

From the repository root, start the backend:

```bash
cd backend
./mvnw spring-boot:run
```

In a separate terminal, from the repository root:

```bash
cd frontend
npm ci
npm run dev
```

Open the local URL printed by Vite, normally http://localhost:5173.

The Vite development proxy forwards `/api` requests to the backend on port 8080.

## Usage

1. Enter sample employee skills.
2. Select Verified or Unverified for each skill.
3. Select Find opportunities.
4. Review matching, missing, and unverified skills and career advice.

Use fictional data for the Alpha demonstration. Verification status is selected manually and does not represent an actual verification process.

## API Interface

### Request

`POST /api/matches/with-advice`

Content type: `application/json`

```json
{
  "employeeSkills": [
    {
      "name": "Java",
      "verificationStatus": "VERIFIED"
    },
    {
      "name": "Git",
      "verificationStatus": "UNVERIFIED"
    }
  ]
}
```

The frontend requires a nonblank skill name and retains at least one skill entry. The backend rejects empty skill lists and null list entries.

### Response

The response contains:

- `matches`: opportunity results with `opportunityId`, `title`, `readiness`, `matchingSkills`, `missingSkills`, and `unverifiedSkills`.
- `aiAdvice`: `recommendation`, `model`, and `aiGenerated`.

Example response structure with AI unavailable:

```json
{
  "matches": [
    {
      "opportunityId": "software-engineer",
      "title": "Software Engineer",
      "readiness": "GROWTH_OPPORTUNITY",
      "matchingSkills": ["Java"],
      "missingSkills": ["REST APIs"],
      "unverifiedSkills": ["Git"]
    }
  ],
  "aiAdvice": {
    "recommendation": "The AI recommendation service is temporarily unavailable.",
    "model": "configured-model",
    "aiGenerated": false
  }
}
```

The example illustrates one opportunity. The backend may return additional opportunities.

### Errors and Fallback

- HTTP 400: displays a message asking the user to check the skills.
- Other unsuccessful responses: displays a retry message.
- Connection failure: displays an error message.
- `aiGenerated: false`: displays an AI availability message while retaining the skill matches.

## AI Configuration

Configure `GEMINI_API_KEY` in the backend environment before starting the backend to enable live advice. Never place the key in frontend code or commit it to Git.

Without an available AI service, the interface can still display deterministic skill matches.

## Build and Lint

From the frontend directory:

```bash
npm run build
npm run lint
```

The production build is written to `dist`.

## Alpha Limitations

- Opportunities use sample data.
- Employee profiles and results are not saved.
- Live AI advice requires backend configuration and provider availability.
- Advice does not yet provide a structured 30/60/90-day development plan.
- The Vite proxy supports local development. Production hosting requires separate API routing configuration.