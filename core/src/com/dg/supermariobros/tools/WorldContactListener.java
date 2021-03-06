package com.dg.supermariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.sprites.Goku;
import com.dg.supermariobros.sprites.enemies.Enemy;
import com.dg.supermariobros.sprites.items.Item;
import com.dg.supermariobros.sprites.tileobjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (collisionDef) {
            case MainGame.GOKU_HEAD_BIT | MainGame.BRICK_BIT:
            case MainGame.GOKU_HEAD_BIT | MainGame.COIN_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.GOKU_HEAD_BIT)
                    ((InteractiveTileObject) fixtureB.getUserData()).onHeadHit((Goku) fixtureA.getUserData());
                else
                    ((InteractiveTileObject) fixtureA.getUserData()).onHeadHit((Goku) fixtureB.getUserData());
                break;
            case MainGame.ENEMY_HEAD_BIT | MainGame.GOKU_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixtureA.getUserData()).hitOnHead((Goku) fixtureB.getUserData());
                else
                    ((Enemy) fixtureB.getUserData()).hitOnHead((Goku) fixtureA.getUserData());
                break;
            case MainGame.ENEMY_BIT | MainGame.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == MainGame.ENEMY_BIT)
                    ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MainGame.GOKU_BIT | MainGame.HOLE_BIT:
            case MainGame.GOKU_BIT | MainGame.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.GOKU_BIT)
                    ((Goku) fixtureA.getUserData()).hit((Enemy) fixtureB.getUserData());
                else
                    ((Goku) fixtureB.getUserData()).hit((Enemy) fixtureA.getUserData());
                break;
            case MainGame.ENEMY_BIT | MainGame.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.GOKU_BIT)
                    ((Enemy) fixtureA.getUserData()).onEnemyHit((Enemy) fixtureB.getUserData());
                else
                    ((Enemy) fixtureB.getUserData()).onEnemyHit((Enemy) fixtureA.getUserData());
                break;
            case MainGame.ITEM_BIT | MainGame.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item) fixtureA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MainGame.ITEM_BIT | MainGame.GOKU_BIT:
                if(fixtureA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item) fixtureA.getUserData()).use((Goku) fixtureB.getUserData());
                else
                    ((Item) fixtureB.getUserData()).use((Goku) fixtureA.getUserData());
                break;
            case MainGame.GOKU_BIT | MainGame.FLAGPOLE_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.GOKU_BIT)
                    ((Goku) fixtureA.getUserData()).win();
                else
                    ((Goku) fixtureB.getUserData()).win();
                break;
            case MainGame.GOKU_BIT | MainGame.INVISIBLE_BIT:
                if (fixtureA.getFilterData().categoryBits == MainGame.GOKU_BIT)
                    ((Goku) fixtureA.getUserData()).goToTheCastle();
                else
                    ((Goku) fixtureB.getUserData()).goToTheCastle();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) { }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
