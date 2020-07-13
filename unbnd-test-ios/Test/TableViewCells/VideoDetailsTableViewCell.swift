//
//  VideoDetailsTableViewCell.swift
//  Test
//
//  Created by Sherjeel on 12/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import UIKit

class VideoDetailsTableViewCell: UITableViewCell {

    @IBOutlet weak var uiImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
   }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
  }

    func set(videoDetails: VideoDetailsModel)  {
        titleLabel.text = videoDetails.title
        uiImageView!.setImageFromUrl(urlStr: videoDetails.thumbnail!)
    }
}
