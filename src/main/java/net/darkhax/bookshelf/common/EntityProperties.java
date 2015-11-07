package net.darkhax.bookshelf.common;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.common.network.packet.PacketSyncPlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityProperties implements IExtendedEntityProperties {
    
    public static final String PROP_NAME = "BookshelfData";
    public final EntityLivingBase entity;
    private NBTTagList buffs = new NBTTagList();
    
    private EntityProperties(EntityLivingBase entity) {
        
        this.entity = entity;
    }
    
    @Override
    public void saveNBTData (NBTTagCompound compound) {
        
        NBTTagCompound entityData = new NBTTagCompound();
        entityData.setTag("BookshelfBuff", buffs);
        compound.setTag(PROP_NAME, entityData);
    }
    
    @Override
    public void loadNBTData (NBTTagCompound compound) {
        
        NBTTagCompound playerData = compound.getCompoundTag(PROP_NAME);
        this.buffs = playerData.getTagList("BookshelfBuff", 10);
    }
    
    @Override
    public void init (Entity entity, World world) {
    
    }
    
    public void sync () {
        
        Bookshelf.network.sendToAll(new PacketSyncPlayerProperties(this));
    }
    
    public static EntityProperties getProperties (EntityLivingBase entity) {
        
        return (EntityProperties) entity.getExtendedProperties(PROP_NAME);
    }
    
    public static EntityProperties setProperties (EntityLivingBase entity) {
        
        entity.registerExtendedProperties(PROP_NAME, new EntityProperties(entity));
        return getProperties(entity);
    }
    
    public static boolean hasProperties (EntityLivingBase entity) {
        
        return getProperties(entity) != null;
    }
    
    public NBTTagList getBuffs () {
        
        return buffs;
    }
    
    public void setBuffs (NBTTagList buffs) {
        
        this.buffs = buffs;
    }
    
}