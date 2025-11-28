# cats_backend/models.py
from datetime import date
from typing import List
from pydantic import BaseModel


class AdoptedCat(BaseModel):
    id: int
    imageUrl: str


class UserProfile(BaseModel):
    id: int
    name: str
    birthdate: date
    email: str
    phone: str
    role: str          # "owner" or "customer"
    adoptedCats: List[AdoptedCat]


class AuthRequest(BaseModel):
    username: str
    password: str


class AuthResponse(BaseModel):
    userId: int
    username: str
//