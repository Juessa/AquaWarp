package vn.anhcraft.aquawarp.API;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;

public class Files {
    public static File config_;
    public static File messages_;
    public static FileConfiguration config;
    public static FileConfiguration messages;
    
    private static void copy(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }    
    
    // aquawarp.html
	public static void new_aquawarphtml(AquaWarp v) {
    	config_ = new File(Options.plugin.dir + Options.files.aquawarphtml);
    	if(!config_.exists()){
    		InputStream stream = v.getClass().getResourceAsStream("/aquawarp.html");
	    	try {
				copy(stream, config_);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}		
	}
    
    // tạo config
	public static final void new_config(AquaWarp v){
    	config_ = new File(Options.plugin.dir + Options.files.config);
    	if(!config_.exists()){
    		InputStream stream = v.getClass().getResourceAsStream("/config.yml");
	    	try {
				copy(stream, config_);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	  	
        config = YamlConfiguration.loadConfiguration(config_);
    }
    
    // tạo thông báo
	public static final void new_messages(AquaWarp v){
    	String[] langs = Options.plugin.langs;	
	
    	for(int i = 0; i < langs.length; i++){
    		String s = langs[i];
			File f = new File(Options.plugin.dir + "messages/message_"+s+".yml");
			if(!f.exists()){					
				try {
					copy(v.getClass().getResourceAsStream("/message_"+s+".yml"), f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
    	}
    	
    	// lấy file lang mặc định
    	messages_ = new File(Options.plugin.dir + "messages/" + config.getString("lang"));
        messages = YamlConfiguration.loadConfiguration(messages_);
    }
    
    // lưu config
    public static final void save_config(){
        try {
            config.save(config_);
        }
        catch (IOException e){
        	e.printStackTrace();
        }
    }
    
    // lưu thông báo
    public static final void save_messages(){
        try {
        	messages.save(messages_);
        }
        catch (IOException e){
        	e.printStackTrace();
        }
    }
    
    // lưu tất cả
    public static final void saveAll(){
    	save_messages();
    	save_config();
    }
    
    // tải lại toàn bộ
    public static final void reload(AquaWarp v){
    	new_messages(v);
    	new_config(v);
    	MySQLFuncs.reConnect();
    }
}
