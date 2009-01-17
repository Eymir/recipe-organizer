package net.bilnoski.recipebook.db.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.bilnoski.recipebook.model.Cookbook;
import net.bilnoski.recipebook.model.Ingredient;
import net.bilnoski.recipebook.model.Recipe;
import net.bilnoski.recipebook.properties.PropertyKey;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RecipeLoader extends DefaultHandler
{
   private final Cookbook cb;
   private Recipe currRecipe;
   private CharacterHandler charHandler;

   public RecipeLoader(Cookbook cb)
   {
      this.cb = cb;
   }
   
   @Override
   public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
   {
      try {
   //      System.out.println("start");
         if (name.equals("recipe"))
         {
            currRecipe = new Recipe();
            return;
         }
         if (currRecipe == null)
            return;
         
         if (name.equals("title"))
         {
            charHandler = new TitleHandler();
            return;
         }
         
         if (name.equals("ingredient"))
         {
            IngredientAttributeHandler ah = new IngredientAttributeHandler();
            ah.process(attributes);
            if (currRecipe.ingredients == null)
               currRecipe.ingredients = new ArrayList<Ingredient>();
            currRecipe.ingredients.add(ah.getIngredient());
         }
         
         if (name.equals("label"))
         {
            LabelAttributeHandler ah = new LabelAttributeHandler();
            ah.process(attributes);
         }
      }
      catch (Exception e)
      {
         throw new SAXException(e);
      }
   }
   
   @Override
   public void characters(char[] ch, int start, int length) throws SAXException
   {
      if (charHandler == null)
         return;
      try {
         char[] src = new char[length];
         System.arraycopy(ch, start, src, 0, length);
         charHandler.process(new String(src));
         charHandler = null;
      } catch (Exception e) {
         throw new SAXException(e);
      }
   }
   
   @Override
   public void endElement(String uri, String localName, String name) throws SAXException
   {
      if (currRecipe == null)
         return;
      if (name.equals("recipe"))
      {
         cb.add(currRecipe);
         return;
      }
      
      if (name.equalsIgnoreCase("ingredientList"))
      {
         if (currRecipe.ingredients != null && !currRecipe.ingredients.isEmpty())
         {
            Comparator<Ingredient> comp = new Comparator<Ingredient>() {
               public int compare(Ingredient a, Ingredient b) {
                  return a.ordinal - b.ordinal;
               }
            };
            Collections.sort(currRecipe.ingredients, comp);
         }
      }
//      System.out.println("end");
   }
   
   interface CharacterHandler
   {
      void process(String s) throws Exception;
   }
   
   class TitleHandler implements CharacterHandler
   {
      public void process(String s)
      {
         currRecipe.title = s;
      }
   }
   
   interface AttributeHandler
   {
      void process(Attributes a) throws Exception;
   }
   
   class IngredientAttributeHandler implements AttributeHandler
   {
      Ingredient ing;
      
      public Ingredient getIngredient()
      {
         return ing;
      }
      
      public void process(Attributes a) throws Exception
      {
         ing = new Ingredient();
         for (int i=0; i<a.getLength(); ++i)
         {
            String n = a.getLocalName(i);
            String val = a.getValue(i);
            if (n.equals("ordinal"))
               ing.ordinal = Integer.parseInt(val);
            else if (n.equals("quantity"))
               ing.qty = Double.parseDouble(val);
            else if (n.equals("kind"))
               ing.kind = val;
            else if (n.equals("unit"))
               ing.unit = val;
            else if (n.equals("preparation"))
               ing.preparation = val;
         }
         if (ing.ordinal <= 0)
            throw new Exception("Ingredient missing ordinal: "+currRecipe.title);
         if (ing.qty <= 0)
            throw new Exception("Ingredient missing quantity: "+currRecipe.title);
         if (ing.kind == null || ing.kind.isEmpty())
            throw new Exception("Ingredient missing kind: "+currRecipe.title);
      }
   }
   
   class LabelAttributeHandler implements AttributeHandler
   {
      public void process(Attributes a) throws Exception
      {
         String k = a.getValue("name");
         String v = a.getValue("value");
         if (k == null)
            throw new Exception("Label missing name: "+currRecipe.title);
         
         currRecipe.props.add(new PropertyKey(k), v);
      }
   }
}
