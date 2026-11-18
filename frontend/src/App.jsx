import { useState } from 'react'
import './App.css'

export default function App() {
  const [skills, setSkills] = useState([
    { name: 'Java', verificationStatus: 'VERIFIED' },
    { name: 'Git', verificationStatus: 'UNVERIFIED' },
  ])
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  function updateSkill(index, field, value) {
    setSkills(current =>
      current.map((skill, i) =>
        i === index ? { ...skill, [field]: value } : skill
      )
    )
    setResult(null)
  }

  async function findMatches(event) {
    event.preventDefault()
    setLoading(true)
    setError('')
    setResult(null)

    try {
      const response = await fetch('/api/matches/with-advice', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          employeeSkills: skills.map(skill => ({
            ...skill,
            name: skill.name.trim(),
          })),
        }),
      })

      if (!response.ok) {
        throw new Error(
          response.status === 400
            ? 'Please check your skills and try again.'
            : 'Unable to retrieve matches. Please try again.'
        )
      }

      setResult(await response.json())
    } catch (err) {
      setError(err.message || 'Unable to connect to the service.')
    } finally {
      setLoading(false)
    }
  }

  function displaySkills(items) {
    return items?.length ? items.join(', ') : 'None'
  }

  return (
    <main className="app">
      <header>
       <header className="brand-header">
  <div className="brand">
    <svg
      className="brand-mark"
      viewBox="0 0 64 64"
      aria-hidden="true"
    >
      {Array.from({ length: 9 }, (_, row) =>
        Array.from({ length: 9 }, (_, col) => {
          const distance = Math.hypot(row - 4, col - 4)
          if (distance > 4.3) return null

          return (
            <circle
              key={`${row}-${col}`}
              cx={8 + col * 6}
              cy={8 + row * 6}
              r={Math.max(0.7, 2 - distance * 0.28)}
              fill="currentColor"
              opacity={Math.max(0.2, 1 - distance * 0.17)}
            />
          )
        })
      )}
    </svg>

    <div>
      <h1>growwithin<span className="brand-ai">AI</span></h1>
      <span className="brand-caption">YOUR NEXT CHAPTER STARTS HERE</span>
    </div>
  </div>

  <span className="release-badge">
    <span className="status-dot" />
    Alpha preview
  </span>
</header>

<div className="intro">
  <span className="eyebrow">CAREER WORKSPACE</span>
  <h2>See where your skills can take you.</h2>
  <p>
    Discover internal opportunities, understand your skill gaps,
    and take your next step with confidence.
  </p>
</div>
      </header>

      <section aria-labelledby="skills-heading">
        <h2 id="skills-heading">Your skills</h2>
        <p>Use sample skills for this Alpha demonstration.</p>

        <form onSubmit={findMatches}>
          {skills.map((skill, index) => (
            <div className="skill-row" key={index}>
              <label>
                Skill {index + 1}
                <input
                  value={skill.name}
                  required
                  pattern=".*\S.*"
                  disabled={loading}
                  onChange={event =>
                    updateSkill(index, 'name', event.target.value)
                  }
                />
              </label>

              <label>
                Verification
                <select
                  value={skill.verificationStatus}
                  disabled={loading}
                  onChange={event =>
                    updateSkill(
                      index,
                      'verificationStatus',
                      event.target.value
                    )
                  }
                >
                  <option value="VERIFIED">Verified</option>
                  <option value="UNVERIFIED">Unverified</option>
                </select>
              </label>

              <button
                type="button"
                disabled={loading || skills.length === 1}
                onClick={() => {
                  setSkills(current => current.filter((_, i) => i !== index))
                  setResult(null)
                }}
              >
                Remove skill {index + 1}
              </button>
            </div>
          ))}

          <div className="actions">
            <button
              type="button"
              disabled={loading}
              onClick={() => {
                setSkills(current => [
                  ...current,
                  { name: '', verificationStatus: 'UNVERIFIED' },
                ])
                setResult(null)
              }}
            >
              Add skill
            </button>

            <button type="submit" disabled={loading}>
              {loading ? 'Finding opportunities...' : 'Find opportunities'}
            </button>
          </div>
        </form>

        {loading && <p role="status">Preparing your results...</p>}
        {error && <p role="alert">{error}</p>}
      </section>

      {result && (
        <section aria-labelledby="results-heading">
          <h2 id="results-heading">Your opportunities</h2>

          {result.matches.length === 0 && <p>No opportunities found.</p>}

          {result.matches.map(match => (
            <article key={match.opportunityId}>
              <h3>{match.title}</h3>
              <p>
                <strong>Readiness:</strong>{' '}
                {match.readiness.replaceAll('_', ' ')}
              </p>
              <p>
                <strong>Matching skills:</strong>{' '}
                {displaySkills(match.matchingSkills)}
              </p>
              <p>
                <strong>Missing skills:</strong>{' '}
                {displaySkills(match.missingSkills)}
              </p>
              <p>
                <strong>Unverified skills:</strong>{' '}
                {displaySkills(match.unverifiedSkills)}
              </p>
            </article>
          ))}

          {result.aiAdvice && (
            <article>
              <h3>Career advice</h3>
              {!result.aiAdvice.aiGenerated && (
                <p>AI advice is currently unavailable. Your skill matches are shown above.</p>
              )}
              {result.aiAdvice.aiGenerated && (
                <p>{result.aiAdvice.recommendation}</p>
              )}
            </article>
          )}
        </section>
      )}
    </main>
  )
}