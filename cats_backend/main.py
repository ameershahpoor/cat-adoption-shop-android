from fastapi import FastAPI, HTTPException
from models import UserProfile
from database import init_fake_data, get_user_by_id

app = FastAPI(title="CatsAdoption IDOR backend")

init_fake_data()

@app.get("/users/{user_id}", response_model=UserProfile)
def get_user(user_id: int):
    """Returns a user profile or an empty profile if not found."""
    record = get_user_by_id(user_id)
    if not record:
        # Instead of raising an error, return an empty profile.
        # This prevents the Android app from crashing.
        return UserProfile(
            name="User Not Found",
            birthdate="",
            email="",
            phone_number="",
            adopted_cats=[]
        )
    return record.profile
