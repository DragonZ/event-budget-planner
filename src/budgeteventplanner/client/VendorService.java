package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendor")
public interface VendorService extends RemoteService {
	void createVendor(String vendorName, String location) throws NoSuchAlgorithmException;

}
