# Workout Plan API Documentation

## Overview

The Workout Plan API allows users to create and manage structured workout plans with multi-day cycles. Plans can contain workout days (with exercises, sets, reps, and weight targets) and rest days.

## Base URL

```
/api/v1/plans
```

## Authentication

Uses JWT token authentication (Firebase Auth). The JWT token is automatically injected into controller methods.

---

## Endpoints

### 1. Create Plan

Create a new workout plan for the authenticated user.

**Endpoint:** `POST /api/v1/plans`

**Headers:**
- `Authorization`: Bearer {JWT token} (required)
- `Content-Type`: application/json

**Request Body:**

```json
{
  "name": "PPL 8-Day Split",
  "description": "Push Pull Legs with 2 rest days per cycle",
  "days": [
    {
      "dayIndex": 0,
      "name": "Push Day 1",
      "type": "WORKOUT",
      "exercises": [
        {
          "exerciseId": "550e8400-e29b-41d4-a716-446655440000",
          "orderIndex": 0,
          "targetSets": 3,
          "targetReps": 10,
          "targetWeight": 100.0,
          "notes": "Focus on form"
        },
        {
          "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
          "orderIndex": 1,
          "targetSets": 3,
          "targetReps": 12,
          "targetWeight": 80.0,
          "notes": null
        }
      ],
      "notes": "High volume day"
    },
    {
      "dayIndex": 1,
      "name": "Pull Day 1",
      "type": "WORKOUT",
      "exercises": [...]
    },
    {
      "dayIndex": 2,
      "name": "Legs Day 1",
      "type": "WORKOUT",
      "exercises": [...]
    },
    {
      "dayIndex": 3,
      "name": "Rest",
      "type": "REST",
      "exercises": [],
      "notes": "Active recovery - walk or stretch"
    },
    {
      "dayIndex": 4,
      "name": "Push Day 2",
      "type": "WORKOUT",
      "exercises": [...]
    },
    {
      "dayIndex": 5,
      "name": "Pull Day 2",
      "type": "WORKOUT",
      "exercises": [...]
    },
    {
      "dayIndex": 6,
      "name": "Legs Day 2",
      "type": "WORKOUT",
      "exercises": [...]
    },
    {
      "dayIndex": 7,
      "name": "Rest",
      "type": "REST",
      "exercises": [],
      "notes": null
    }
  ]
}
```

**Response:** `201 Created`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "PPL 8-Day Split",
  "description": "Push Pull Legs with 2 rest days per cycle",
  "cycleDays": 8,
  "isActive": false,
  "activationDate": null,
  "days": [
    {
      "id": "234e5678-e89b-12d3-a456-426614174001",
      "dayIndex": 0,
      "name": "Push Day 1",
      "type": "WORKOUT",
      "exercises": [
        {
          "id": "345e6789-e89b-12d3-a456-426614174002",
          "exerciseId": "550e8400-e29b-41d4-a716-446655440000",
          "orderIndex": 0,
          "targetSets": 3,
          "targetReps": 10,
          "targetWeight": 100.0,
          "targetFormatted": "3x10 @ 100.0kg",
          "notes": "Focus on form"
        }
      ],
      "notes": "High volume day"
    }
  ],
  "createdAt": "2025-01-15T10:30:00Z",
  "updatedAt": null
}
```

**Validation Rules:**
- `name`: Required, not blank
- `days`: Required, not empty, 1-30 days
- `dayIndex`: Sequential from 0 to N-1
- `type`: Must be "WORKOUT" or "REST"
- WORKOUT days must have at least 1 exercise
- REST days must have 0 exercises
- `targetSets`: Must be positive
- `targetReps`: Must be positive if provided
- `targetWeight`: Must be non-negative if provided

---

### 2. List User Plans

Get all plans created by the authenticated user.

**Endpoint:** `GET /api/v1/plans`

**Headers:**
- `Authorization`: Bearer {JWT token} (required)

**Response:** `200 OK`

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "PPL 8-Day Split",
    "description": "Push Pull Legs with 2 rest days per cycle",
    "cycleDays": 8,
    "workoutDays": 6,
    "restDays": 2,
    "isActive": true,
    "activationDate": "2025-01-10",
    "createdAt": "2025-01-15T10:30:00Z"
  },
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "name": "Upper Lower 4-Day",
    "description": null,
    "cycleDays": 4,
    "workoutDays": 4,
    "restDays": 0,
    "isActive": false,
    "activationDate": null,
    "createdAt": "2025-01-12T14:20:00Z"
  }
]
```

---

### 3. Get Plan Details

Get detailed information about a specific plan.

**Endpoint:** `GET /api/v1/plans/{id}`

**Path Parameters:**
- `id`: Plan UUID

**Response:** `200 OK`

Same structure as Create Plan response.

**Error Responses:**
- `404 Not Found`: Plan doesn't exist

---

### 4. Get Active Plan

Get the user's currently active plan.

**Endpoint:** `GET /api/v1/plans/active`

**Headers:**
- `Authorization`: Bearer {JWT token} (required)

**Response:** `200 OK`

Same structure as Create Plan response.

**Error Responses:**
- `404 Not Found`: User has no active plan

---

## Domain Model

### DayType Enum

- `WORKOUT`: A day with scheduled exercises
- `REST`: A rest/recovery day (no exercises)

### Plan Cycle Logic

Plans have a configurable cycle length (`cycleDays`). The client calculates which day of the cycle the user is on using:

```
daysSinceActivation = today - activationDate
currentDayIndex = daysSinceActivation % cycleDays
```

For example, with an 8-day PPL cycle:
- Day 0: Push 1
- Day 1: Pull 1
- Day 2: Legs 1
- Day 3: Rest
- Day 4: Push 2
- Day 5: Pull 2
- Day 6: Legs 2
- Day 7: Rest
- Day 8: Wraps back to Push 1 (index 0)

---

## Example: Create Simple 3-Day Plan

```bash
curl -X POST http://localhost:8080/api/v1/plans \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Simple 3-Day Full Body",
    "description": "Beginner full body workout",
    "days": [
      {
        "dayIndex": 0,
        "name": "Day 1",
        "type": "WORKOUT",
        "exercises": [
          {
            "exerciseId": "550e8400-e29b-41d4-a716-446655440000",
            "orderIndex": 0,
            "targetSets": 3,
            "targetReps": 10,
            "targetWeight": null,
            "notes": null
          }
        ],
        "notes": null
      },
      {
        "dayIndex": 1,
        "name": "Rest",
        "type": "REST",
        "exercises": [],
        "notes": "Take it easy"
      },
      {
        "dayIndex": 2,
        "name": "Day 2",
        "type": "WORKOUT",
        "exercises": [
          {
            "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
            "orderIndex": 0,
            "targetSets": 4,
            "targetReps": 8,
            "targetWeight": 80.0,
            "notes": null
          }
        ],
        "notes": null
      }
    ]
  }'
```

---

## Error Handling

All errors follow this format:

```json
{
  "message": "Error summary",
  "details": "Detailed error description"
}
```

### Common Error Codes

- `400 Bad Request`: Invalid request data or validation error
- `404 Not Found`: Plan or referenced resource not found
- `409 Conflict`: Operation conflicts with current state
- `500 Internal Server Error`: Unexpected server error

### Specific Errors

**Invalid Exercise Reference:**
```json
{
  "message": "Invalid exercise reference in plan",
  "details": "The exercise ID '550e8400-e29b-41d4-a716-446655440000' does not exist"
}
```

**Plan Not Found:**
```json
{
  "message": "Plan not found",
  "details": "The plan with ID '123e4567-e89b-12d3-a456-426614174000' does not exist"
}
```

---

## Database Schema

Plans are stored across three tables:

1. **workout_plans**: Plan metadata
   - id, user_id, name, description, cycle_days
   - is_active, activation_date
   - created_at, updated_at

2. **plan_days**: Days within the plan
   - id, plan_id, day_index, name, day_type
   - notes

3. **plan_exercises**: Exercises for workout days
   - id, plan_day_id, exercise_id, order_index
   - target_sets, target_reps, target_weight
   - notes

---

## Next Steps (Not Yet Implemented)

- Plan activation endpoint (PUT /api/v1/plans/{id}/activate)
- Plan deactivation endpoint (PUT /api/v1/plans/{id}/deactivate)
- Update plan endpoint (PUT /api/v1/plans/{id})
- Delete plan endpoint (DELETE /api/v1/plans/{id})
- Clone template endpoint (POST /api/v1/plans/templates/{id}/clone)
- List global templates endpoint (GET /api/v1/plans/templates)
