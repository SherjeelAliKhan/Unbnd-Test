//
//  Extensions.swift
//  Test
//
//  Created by Sherjeel on 12/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import Foundation
import Kingfisher
import UIKit

extension UIImageView {
    
    func setImageFromUrl(urlStr:String){
        let urlStr = urlStr.replacingOccurrences(of: " ", with: "%20")
        
        let url = URL(string: urlStr)!
        
        print(url)
        self.kf.indicatorType = .activity
        self.kf.setImage(with: url,
                         placeholder: nil,
                         options: [
                            .scaleFactor(UIScreen.main.scale),
                            .transition(.fade(1)),
                            .cacheOriginalImage
                            ],
                         progressBlock: nil,
                         completionHandler: nil)
    }
    
    func setImageFromUrlWithoutSave(urlStr:String){
        let url = URL(string: urlStr)!
        //print(url)
        
        ImageDownloader.default.downloadImage(with: url, options: [], progressBlock: nil) {
            (image, error, url, data) in
            self.image = image
        }
    }
}
