package handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ModerationHandler {

	/**
	 * Used to create a HashMap to set permissions
	 *
	 * @param ids is a 2D array that contains an array of IDs
	 * @param isRole is a boolean value that determines if the IDs provided are type Role, OTHERWISE it MUST be type User
	 * @return a HashMap of the key and IDs as designated
	 */
	public static HashMap<String, ArrayList<Long>> obtainHashmap(Long[][] ids, boolean isRole) {

		HashMap<String, ArrayList<Long>> returnThis = new HashMap<String, ArrayList<Long>>();

		ArrayList<Long> tempArr = new ArrayList<Long>();
		for(Long[] arr: ids) {
			for(Long id: arr) {
				Collections.addAll(tempArr, id);
			}
		}

		if(isRole)
			returnThis.put("ROLE", tempArr);
		else
			returnThis.put("USER", tempArr);

		return returnThis;

	}

}
