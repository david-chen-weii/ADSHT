package com.thur.demo.model.group;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageData {
	
	private int groupId;
	
    private String image;
    
    private String content;
    
    private UUID sender_id;
    
    private String memberImage;
    
    private String memberName;
    
}
