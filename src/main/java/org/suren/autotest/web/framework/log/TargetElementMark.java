/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

/**
 * @author suren
 * @since 2017年6月29日 下午3:53:51
 */
public class TargetElementMark implements ElementMark
{

	@Override
	public void mark(WebElement ele, File file) throws IOException
	{
		BufferedImage bufImg = ImageIO.read(file);
		
		try
		{
			WebElement webEle = (WebElement) ele;
			Point loc = webEle.getLocation();
			Dimension size = webEle.getSize();
			
			Graphics2D g = bufImg.createGraphics();
			g.setColor(Color.red);
			g.drawRect(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
		}
		catch(StaleElementReferenceException se)
		{
		}
	}

}
