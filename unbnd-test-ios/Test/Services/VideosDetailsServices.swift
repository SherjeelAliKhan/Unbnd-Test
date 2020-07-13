//
//  VideoDetailsService.swift
//  Test
//
//  Created by Sherjeel on 12/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import Foundation
import SwiftyJSON
import Alamofire

class VideosDetailsServices {
    
    static var debugging = Debugging.isOn
    
    static func GetDetails(completionHandler:@escaping (_ success:Bool,_ response:[VideoDetailsModel]?, _ error:Any?)->Void){
        
        if(debugging) {  print("VideosDetailsServices - GetDetails") }
        
        HttpHelper.GetRequest(fromSavedUrl: API.videosDetailsURL , parameters: [:], callback: { (response, error) in
            
            if(debugging) {  print("VideosDetailsServices - HttpHelper Response Received") }
            
            let res = response!.array!;
            
            if error != nil {
                if(debugging) {  print("VideosDetailsServices - HttpHelper Error Response:\n" + String(describing: (error?.localizedDescription)!)) }
                completionHandler(false,nil, error)
                return
            }
            
            if(res.count > 0 && debugging){ print("VideosDetailsServices - Data Exists") }
            
            var videosDetailsModel : [VideoDetailsModel] = []
            for data in res {
                videosDetailsModel.append(VideoDetailsModel.init(json: data))
            }
            
            completionHandler(true,videosDetailsModel, nil)
        })
    }
}
