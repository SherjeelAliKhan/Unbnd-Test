//
//  Alert.swift
//  Test
//
//  Created by Sherjeel on 13/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import MBProgressHUD

class Loader: NSObject {
    
    static var loadingAlert : MBProgressHUD!
    
    static func showLoader(message : String){
        loadingAlert = MBProgressHUD.showAdded(to: AppDelegate.getInstatnce().window!, animated: true)
        loadingAlert.label.text = message
    }
    
    static func hideLoader(){
        MBProgressHUD.hide(for: AppDelegate.getInstatnce().window!, animated: true)
    }
}
