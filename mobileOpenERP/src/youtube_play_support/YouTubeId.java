/**
 * 
 */
package youtube_play_support;

public abstract class YouTubeId {
	protected String mId;
	public YouTubeId(String pId){
		mId = pId;
	}
	
	public String getId(){
		return mId;
	}
}