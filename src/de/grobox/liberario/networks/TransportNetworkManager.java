package de.grobox.liberario.networks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import de.grobox.liberario.data.RecentsDB;
import de.grobox.liberario.settings.SettingsManager;
import de.schildbach.pte.NetworkId;
import de.schildbach.pte.dto.Location;

public class TransportNetworkManager {

	private final Context context;
	private final SettingsManager settingsManager;

	@Nullable
	private TransportNetwork transportNetwork, transportNetwork2, transportNetwork3;
	@Nullable
	private Location home;

	@Inject
	public TransportNetworkManager(Context context, SettingsManager settingsManager) {
		this.context = context;
		this.settingsManager = settingsManager;
		transportNetwork = loadTransportNetwork(1);
		transportNetwork2 = loadTransportNetwork(2);
		transportNetwork3 = loadTransportNetwork(3);
		loadHome();
	}

	@Nullable
	private TransportNetwork loadTransportNetwork(int i) {
		NetworkId networkId = settingsManager.getNetworkId(i);

		if (networkId != null) {
			for (TransportNetwork network : TransportNetworks.networks) {
				if (network.getId().equals(networkId)) return network;
			}
		}
		return null;
	}

	@Nullable
	public TransportNetwork getTransportNetwork() {
		return getTransportNetwork(0);
	}

	@Nullable
	public TransportNetwork getTransportNetwork(int i) {
		if (i == 0 || i == 1) {
			return transportNetwork;
		} else if (i == 2) {
			return transportNetwork2;
		} else if (i == 3) {
			return transportNetwork3;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Nullable
	public Location getHome() {
		return home;
	}

	public void setHome(@Nullable final Location home) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				TransportNetworkManager.this.home = home;
			}
		});
	}

	private void loadHome() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				setHome(RecentsDB.getHome(context));
			}
		}).start();
	}

}
