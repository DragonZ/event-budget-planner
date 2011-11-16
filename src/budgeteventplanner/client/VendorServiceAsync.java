package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void createVendor(String vendorName, String location, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;
	
}
