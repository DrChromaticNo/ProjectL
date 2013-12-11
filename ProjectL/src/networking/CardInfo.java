package networking;

import java.awt.Color;
import java.io.Serializable;

/**
 * Class to hold the transmittable information about a given card
 * @author Chris
 *
 */

public class CardInfo implements Serializable {
	
	private static final long serialVersionUID = 3940516493620273210L;
	private Color faction;
	private int value;
	private int silver;
	private String abbrv;
	private String name;
	private String desc;
	
	public CardInfo (Color faction, int val, int s, String abbrv, String name, String desc)
	{
		this.faction = faction;
		value = val;
		silver = s;
		this.abbrv = abbrv;
		this.name = name;
		this.desc = desc;
	}
	
	public Color getFaction()
	{
		return faction;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getSilver()
	{
		return silver;
	}
	
	public String getAbbrv()
	{
		return abbrv;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		if(arg0 instanceof CardInfo)
		{
			CardInfo info2 = (CardInfo) arg0;
			if(this.faction.equals(info2.faction))
			{
				if(this.value == info2.value)
				{
					if(this.silver == info2.silver)
					{
						if(this.abbrv.equals(info2.abbrv))
						{
							if(this.name.equals(info2.name))
							{
								if(this.desc.equals(info2.desc))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return faction.hashCode()*value*silver*abbrv.hashCode()*name.hashCode()*desc.hashCode();
	}
	
}
