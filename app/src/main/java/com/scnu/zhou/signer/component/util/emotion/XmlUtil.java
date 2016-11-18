package com.scnu.zhou.signer.component.util.emotion;

import android.util.Xml;

import com.scnu.zhou.signer.component.bean.chat.Emotion;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {
	public static List<Emotion> getEmotions(InputStream inputStream) {
		XmlPullParser parser = Xml.newPullParser();
		int eventType = 0;
		List<Emotion> emotions = null;
		Emotion emotion = null;
		try {
			parser.setInput(inputStream, "UTF-8");
			eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					emotions = new ArrayList<Emotion>();
					break;
				case XmlPullParser.START_TAG:
					if ("emotion".equals(parser.getName())) {
						emotion = new Emotion();

					} else if ("code".equals(parser.getName())) {
						emotion.setCode(parser.nextText());
					} else if ("name".equals(parser.getName())) {
						emotion.setName(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if ("emotion".equals(parser.getName())) {
						emotions.add(emotion);
						emotion = null;
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emotions;
	}
}
