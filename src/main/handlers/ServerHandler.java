package main.handlers;
		
import java.util.ArrayList;

@Deprecated
public class ServerHandler {

	private ArrayList<Long> ignoreChannels, ignoreGuilds, ignoreUsers;
	
	public ServerHandler() {
		ignoreChannels = new ArrayList<Long>();
		ignoreGuilds = new ArrayList<Long>();
		ignoreUsers = new ArrayList<Long>();
	}
	
	public ServerHandler(ArrayList<Long> chns, ArrayList<Long> glds, ArrayList<Long> usrs) {
		ignoreChannels = chns;
		ignoreGuilds = glds;
		ignoreUsers = usrs;
	}
	
	//Setter Methods
	public void setIgnChns(ArrayList<Long> chns) {
		ignoreChannels = chns;
	}
	/**Unnecessary IF used in one server.
	 * 
	 * @param glds A list of Guilds to ignore
	 */
	public void setIgnGlds(ArrayList<Long> glds) {
		ignoreGuilds = glds;
	}
	/**Unnecessary IF used in one server
	 * 
	 * @param usrs
	 */
	public void setIgnUsrs(ArrayList<Long> usrs) {
		ignoreUsers = usrs;
	}
	
	//Getter Methods
	public ArrayList<Long> getIgnChns() {
		return ignoreChannels;
	}
	public ArrayList<Long> getIgnGlds() {
		return ignoreGuilds;
	}
	public ArrayList<Long> getIgnUsrs() {
		return ignoreUsers;
	}
	
}
