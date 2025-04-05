## Features

- Lists non-fork repositories for a given GitHub user
- For each repository, provides:
  - Repository name
  - Owner login
  - List of branches with their names and last commit SHA
- Proper error handling for non-existent users (404 response)

## API Endpoint
GET /api/github-repos/{username}

### Successful Response Example

```json
[
    {
        "repositoryName": "repo1",
        "ownerLogin": "user1",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "abc123..."
            },
            {
                "name": "develop",
                "lastCommitSha": "def456..."
            }
        ]
    }
]
```
## Error Response Example
```json
{
    "status": 404,
    "message": "User 'nonexistent-user' not found"
}
```
