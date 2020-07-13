//
//  VideoViewController.swift
//  Test
//
//  Created by Sherjeel on 11/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import UIKit
import GPVideoPlayer
import AVFoundation

class VideoViewController: UIViewController {
    
    @IBOutlet weak var videoTableView: UITableView!
    @IBOutlet weak var videoView: UIView!
    
    let debugging = Debugging.isOn
    let playCustomURL = Debugging.playCustomURL
    let customURL = Debugging.customURL
    
    var videosDetails : [VideoDetailsModel] = []
    var videoPlayer: GPVideoPlayer? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        videoTableView.isHidden = true
        videoTableView.dataSource = nil
        videoTableView.delegate = nil
        self.view.endEditing(true)
        Loader.showLoader(message: "Please wait ...")
        LoadServices()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        //videoView.subviews.forEach({ $0.removeFromSuperview() })
        //videoPlayer?.getAVPlayer()?.removeAllItems()
        //videoPlayer?.removeFromSuperview()
        //videoPlayer = nil
        videoPlayer?.pauseVideo()
    }
    
    func LoadServices() {
        if(debugging) { print("VideoViewController - Loading Service") }
        VideosDetailsServices.GetDetails {(successful, videosDetails: [VideoDetailsModel]?, error) in
            if(successful){
                if videosDetails != nil{
                    if(self.debugging) { print("VideoViewController - Successfully loaded video details") }
                    self.videosDetails = videosDetails!
                    self.ServiceIsLoaded()
                } else {
                    if(self.debugging) { print("VideoViewController - There is no Videos available") }
                    //  Alert.showAlert(title: "", message: "You entered wrong code.")
                }
            } else  {
                if(self.debugging) { print("VideoViewController - Error in loading video details service" ); }
                //  Alert.showAlert(title: "", message: "You entered wrong code.")
            }
        }
    }
    
    func ServiceIsLoaded(){
        videoTableView.isHidden = false
        videoTableView.dataSource = self
        videoTableView.delegate = self
        videoTableView.reloadData()
        
        initializePlayer()
        if(playCustomURL){
            loadViedeo(customURL)
        } else {
            if(videosDetails.count > 0){
                loadViedeo(videosDetails[0].thumbnail!)
            } else {
                if(self.debugging) { print("VideoViewController - There is no Videos available" ); }
            }
        }
    }
}

// ==============================================  Table View  ==============================================

extension VideoViewController : UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return videosDetails.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let videoDetails = videosDetails[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "VideoDetails") as! VideoDetailsTableViewCell
        cell.set(videoDetails: videoDetails)
        let singleTap = TouchRowUITapGestureRecognizer(target: self, action: #selector(self.onVideoDetailsClick))
        singleTap.url = videoDetails.url!
        cell.addGestureRecognizer(singleTap)
        return cell
    }
    
    @objc func onVideoDetailsClick(sender : TouchRowUITapGestureRecognizer) {
        let urlString = sender.url
        if let url = URL(string: urlString) {
            //videoPlayer?.pauseVideo()
            videoPlayer?.getAVPlayer()!.replaceCurrentItem(with: AVPlayerItem(url: url))
        }
    }
    
    class TouchRowUITapGestureRecognizer: UITapGestureRecognizer {
        var url = String()
    }
}

// ==============================================  Video Player  ==============================================
extension VideoViewController {
    
    func initializePlayer(){
        let rect = CGRect.init(x:  videoView.frame.minX, y:  videoView.frame.minY, width: UIScreen.main.bounds.size.width, height: videoView.frame.size.height)
        if let videoPlayer = GPVideoPlayer.initialize(with: rect) {
            self.videoView.addSubview(videoPlayer)
            self.videoPlayer = videoPlayer
            
            videoPlayer.isToShowPlaybackControls = false
            videoPlayer.isMuted = false
            let touchView = UIView(frame: rect)
            self.videoView.addSubview(touchView)
            
            let singleTap = UITapGestureRecognizer(target: self, action:  #selector(self.singleClick))
            touchView.addGestureRecognizer(singleTap)
            
            let doubleTap = UITapGestureRecognizer(target: self, action: #selector(self.doubleClick))
            doubleTap.numberOfTapsRequired = 2
            touchView.addGestureRecognizer(doubleTap)
        }
    }
    
    func loadViedeo(_ urlString:String){
        if let url = URL(string: urlString) {
            if(videoPlayer != nil){
                videoPlayer!.loadVideo(with: url)
                videoPlayer!.playVideo()
                Loader.hideLoader()
            }
        }
    }
    
    @objc func singleClick(sender : UITapGestureRecognizer) {
        if(videoPlayer?.isPlaying ?? false){
            videoPlayer?.pauseVideo()
        } else {
            videoPlayer?.playVideo()
            
        }
    }
    
    @objc func doubleClick(sender : UITapGestureRecognizer) {
        print("double")
    }
}
