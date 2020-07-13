//
//  HttpHelper.swift
//  Test
//
//  Created by Sherjeel on 12/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class HttpHelper: NSObject {
    
    static  var debugging = Debugging.isOn
    
    static func GetRequest(fromSavedUrl url: String, parameters: [String: Any], callback: ((JSON?, Error?) -> Void)?) {
        
        if(debugging) { print("HttpHelper - Request URL: \(url)") }
        
        Alamofire.request(url, method: .get, parameters: parameters, headers: [:]).responseJSON{ (response:DataResponse<Any>) in
            switch(response.result) {
            case .success(_):
                if response.result.isSuccess {
                    if(debugging) { print("HttpHelper - Get Successful Response") }
                    if let value: Any = response.result.value as AnyObject? {
                        let response = JSON(value)
                        callback?(response, nil)
                    }
                } else {
                    if(debugging) { print("HttpHelper - Request failed with error: \(response.result.error!.localizedDescription)") }
                    callback?(nil, response.result.error!)
                }
            case .failure(_):
                if(debugging) { print("HttpHelper - Request failed with error: \(response.result.error!.localizedDescription)") }
                callback?(nil, response.result.error!)
                break
            }
        }
    }
}
