package net.darkhax.bookshelf.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerNBT implements ISerializer<CompoundNBT> {
    
    public static final ISerializer<CompoundNBT> SERIALIZER = new SerializerNBT();
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    private SerializerNBT() {
        
    }
    
    @Override
    public CompoundNBT read (JsonElement json) {
        
        try {
            
            if (json.isJsonObject()) {
                
                return JsonToNBT.getTagFromJson(GSON.toJson(json));
            }
            
            else {
                
                return JsonToNBT.getTagFromJson(JSONUtils.getString(json, "nbt"));
            }
        }
        
        catch (final CommandSyntaxException e) {
            
            throw new JsonParseException("Failed to read NBT from " + JSONUtils.toString(json), e);
        }
    }
    
    @Override
    public JsonElement write (CompoundNBT toWrite) {
        
        return GSON.toJsonTree(toWrite);
    }
    
    @Override
    public CompoundNBT read (PacketBuffer buffer) {
        
        return buffer.readCompoundTag();
    }
    
    @Override
    public void write (PacketBuffer buffer, CompoundNBT toWrite) {
        
        buffer.writeCompoundTag(toWrite);
    }
}