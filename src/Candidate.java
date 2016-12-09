
public class Candidate {
	private String name;
	private String party;
	private String title;
	
	public Candidate(String candName, String candParty, String candTitle)
	{
		name = candName;
		party = candParty;
		title = candTitle;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getParty()
	{
		return party;
	}
	
	public String getTitle()
	{
		return title;
	}
}
