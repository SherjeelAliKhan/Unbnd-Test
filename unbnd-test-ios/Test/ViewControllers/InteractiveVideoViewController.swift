//
//  InteractiveVideoViewController.swift
//  Test
//
//  Created by Sherjeel on 13/07/2020.
//  Copyright © 2020 Sherjeel. All rights reserved.
//

import UIKit
import SceneKit

class InteractiveVideoViewController: UIViewController {
    
    @IBOutlet weak var mySceneView: SCNView!
    override func viewDidLoad() {
        super.viewDidLoad()
        mySceneView.allowsCameraControl = true
        //Create instance of scene
        let scene = SCNScene()
        mySceneView.scene = scene
        //Add cube to scnview
        AddCubeToScene()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func AddCubeToScene() {
        //Create a box of size 0.1 * 0.1 * 0.1
        let myBox = SCNBox(width: 0.1, height: 0.1, length: 0.1, chamferRadius: 0)
        myBox.firstMaterial?.diffuse.contents = UIColor.red
        myBox.firstMaterial?.isDoubleSided = true
        //Create SCnnode of from geometry and specified its position
        let cubeNode = SCNNode(geometry: myBox)
        //Add created scnnode to scene
        mySceneView.scene?.rootNode.addChildNode(cubeNode)
    }
    
}
