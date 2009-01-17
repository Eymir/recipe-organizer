package net.bilnoski.recipebook.internal;

import java.io.IOException;
import java.io.InputStream;

import net.bilnoski.recipebook.model.Cookbook;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class Activator extends AbstractUIPlugin
{
   public static final String PLUGIN_ID = "net.bilnoski.recipebook";
   private static Activator plugin;
   
   private ServiceRegistration reg;

   public Activator()
   {
   }

   @Override
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      plugin = this;
      
      Cookbook cb = new Cookbook();
      reg = context.registerService(Cookbook.class.getCanonicalName(), cb, null);
   }

   @Override
   public void stop(BundleContext context) throws Exception
   {
      if (reg != null)
      {
         try {
            reg.unregister();
            reg = null;
         } catch (Exception e){
            e.printStackTrace();
         }
      }
      
      plugin = null;
      super.stop(context);
   }
   
   @SuppressWarnings("unchecked")
   public <T> T getService(Class<T> cls)
   {
      ServiceReference sr = getBundle().getBundleContext().getServiceReference(cls.getCanonicalName());
      Object svc = getBundle().getBundleContext().getService(sr);
      return (T)svc;
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
   
   public InputStream openStream(String pathStr) throws IOException
   {
      return FileLocator.openStream(getBundle(), Path.fromOSString(pathStr), false);
   }
}
