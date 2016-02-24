package com.gameleonevents.namesis;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	//Objet faisant passer le service Sensoro dans le core du jeu
    ActionResolverAndroid actionResolverAndroid;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Initialisation du Resolver
		actionResolverAndroid = new ActionResolverAndroid(this);
        //Log.v("Test", actionResolverAndroid.toString());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new NamesisGame(actionResolverAndroid), config);
		//initialize(new CounterAttack(), config);
	}

}
