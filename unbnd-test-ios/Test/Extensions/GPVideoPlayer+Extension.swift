//
//  GPVideoPlayer+Extension.swift
//  Test
//
//  Created by Sherjeel on 12/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//
import AVFoundation
import GPVideoPlayer

extension GPVideoPlayer{
    func getAVPlayer() -> AVQueuePlayer? {
        return player
    }
    
    var isPlaying: Bool {
        if(player != nil){
            if (player!.rate != 0 && player!.error == nil) {
                return true
            }}
        return false
        
    }
}
