package net.bilnoski.recipebook.db;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.bilnoski.recipebook.db.internal.RecipeLoader;
import net.bilnoski.recipebook.internal.Activator;
import net.bilnoski.recipebook.model.Cookbook;

public class RecipeDataStore
{
   public void load(Cookbook cb) throws Exception
   {
      cb.clear();
      
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      parser.parse(Activator.getDefault().openStream("res/recipelist.xml"), new RecipeLoader(cb));
   }
}
