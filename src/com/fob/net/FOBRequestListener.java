package com.fob.net;

/**
 * Receive and deal the result of SDK network request
 * @author talaya
 *
 */
public interface FOBRequestListener{
	/**
	 * The callback function when network request complete
	 * @param responseObject The response object of network request
	 * @see FOBResponse
	 */
	public void onYodo1RequestComplete(FOBResponse responseObject);
}
