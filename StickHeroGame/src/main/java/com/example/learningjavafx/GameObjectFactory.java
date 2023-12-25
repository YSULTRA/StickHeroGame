package com.example.learningjavafx;

// GameObjectFactory.java
public class GameObjectFactory {

    //Factory Design Pattern
    public GameObject createGameObject(String objectType) {
        // Factory method to create different game objects based on objectType
        switch (objectType.toLowerCase()) {
            case "cherry":
                return new Cherry();
            case "hero":
                return new Hero();
            default:
                throw new IllegalArgumentException("Unknown object type: " + objectType);
        }
    }
}
