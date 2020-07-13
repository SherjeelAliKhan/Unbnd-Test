//  VideoDetails.swift
//
//  Created by Sherjeel on 12/07/2020
//  Copyright (c) . All rights reserved.
//

import Foundation
import SwiftyJSON

class VideoDetailsModel: NSCoding {
    
    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
        static let url = "url"
        static let title = "title"
        static let thumbnail = "thumbnail"
    }
    
    // MARK: Properties
    public var url: String?
    public var title: String?
    public var thumbnail: String?

    
    // MARK: SwiftyJSON Initializers
    /// Initiates the instance based on the object.
    ///
    /// - parameter object: The object of either Dictionary or Array kind that was passed.
    /// - returns: An initialized instance of the class.
    public convenience init(object: Any) {
        self.init(json: JSON(object))
    }
    
    
    /// Initiates the instance based on the JSON that was passed.
    ///
    /// - parameter json: JSON object from SwiftyJSON.
    public required init(json: JSON) {
        url = json[SerializationKeys.url].string
        title = json[SerializationKeys.title].string
        thumbnail = json[SerializationKeys.thumbnail].string
    }
    

    
    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = url { dictionary[SerializationKeys.url] = value }
        if let value = title { dictionary[SerializationKeys.title] = value }
        if let value = thumbnail { dictionary[SerializationKeys.thumbnail] = value }
        return dictionary
    }
    
    // MARK: NSCoding Protocol
    required public init(coder aDecoder: NSCoder) {
        self.url = aDecoder.decodeObject(forKey: SerializationKeys.url) as? String
        self.title = aDecoder.decodeObject(forKey: SerializationKeys.title) as? String
         self.thumbnail = aDecoder.decodeObject(forKey: SerializationKeys.thumbnail) as? String
    }
    
    public func encode(with aCoder: NSCoder) {
        aCoder.encode(url, forKey: SerializationKeys.url)
        aCoder.encode(title, forKey: SerializationKeys.title)
        aCoder.encode(thumbnail, forKey: SerializationKeys.thumbnail)
    }
}

