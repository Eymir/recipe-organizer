package net.bilnoski.recipebook.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin
{
   public static final String PLUGIN_ID = "net.bilnoski.recipebook";
   private static Activator plugin;

   public Activator()
   {
   }

   @Override
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      plugin = this;
   }

   @Override
   public void stop(BundleContext context) throws Exception
   {
      plugin = null;
      super.stop(context);
   }

   public static Activator getDefault()
   {
      return plugin;
   }

   /**
    * Returns an image descriptor for the image file at the given plug-in relative path
    *
    * @param path the path
    * @return the image descriptor
    */
   public static ImageDescriptor getImageDescriptor(String path)
   {
      return imageDescriptorFromPlugin(PLUGIN_ID, path);
   }
}
