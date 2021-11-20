package com.dg.supermariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.sprites.Enemy;
import com.dg.supermariobros.sprites.InteractiveTileObject;
import com.dg.supermariobros.sprites.Mario;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        if (fixtureA.getUserData() == "head" || fixtureB.getUserData() == "head") {
            Fixture head = fixtureA.getUserData() == "head" ? fixtureA : fixtureB;
            Fixture object = head == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(
                    object.getUserData().getClass()))
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
        }

        switch (collisionDef) {
            case SuperMarioBros.ENEMY_HEAD_BIT | SuperMarioBros.MARIO_BIT:
                if (fixtureA.getFilterData().categoryBits == SuperMarioBros.ENEMY_HEAD_BIT)
                    ((Enemy) fixtureA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixtureB.getUserData()).hitOnHead();
                break;
            case SuperMarioBros.ENEMY_BIT | SuperMarioBros.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == SuperMarioBros.ENEMY_BIT)
                    ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case SuperMarioBros.MARIO_BIT | SuperMarioBros.ENEMY_BIT:
                Gdx.app.log("mario", "died");

        }
    }

    @Override
    public void endContact(Contact contact) { }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
