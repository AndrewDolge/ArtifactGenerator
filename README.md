# ArtifactGenerator

## The What:

ArtifactGenerator is a CLI program designed to generate random items known as 'artifacts' for use in TTRPGs, or any other fantasy setting. 

Each 'artifact' is a set of categories and descriptions, created by a set of 'Descriptors'. A descriptor is a Category name with a list of entries that can be chosen from. These descriptors are highly configurable from json data files. The user can choose what data to include, the manner of selection, category dependencies, and much more.

ArtifactGenerator currently can output these 'Artifacts' to the command line or to markdown(.md) files.

## The How:

### Build From Source

#### Prerequisities
* Git
* Java 11 or above
#### Steps
1. Clone this repository with the command `git clone https://github.com/AndrewDolge/ArtifactGenerator.git`
2. Move into the directory with: `cd ArtifactGenerator`
3. Run the command: `./gradlew build`
    * This should create the build in the `build` subdirectory.

### CLI Options

* `--custom <descriptor_string>`
    * Adds additional descriptions to the generated Artifacts. All categories and all parts are guaranteed to be added.
    * `<descriptor_string>` should take the form of ` Category=Part1,Part2;"`
        * additional categories can be appended to this option. They should be always terminate with a semicolon(;).
        * Multiple parts can be added to a category. 
            * Categories should be separated by parts with an equals(=).
            *  parts should be a comma(,) separated list.
* `--descriptor <directory>`
    * specifies the directory of files where descriptors and other modifiers live. Currently, .json files are supported.
* `--help`
    * Outputs a helpful string that displays the commands of the program.
* `--markdown <directory>`
    * Outputs the artifact files into the given directory.
    * Artifacts are given a name based on a hash of their description, or a description with the category "Name".

### JSON Configuration

ArtifactGenerator uses .json files to configure individual 'descriptors'.


Provided below is a description of all the different fields that the 'descriptors' can and should include:




| Name | Value | Description | is required? |
| --- | ---  | -------- |  --- | 
|  `category`    |   "String"    |  Sets the category for this descriptor | yes | 
|  `data`    |  a list[] of "Strings"     |        Sets the independent data for this descriptor      | no, if `dependentData` is present.
|   `dependentCategory`   |   "String"    |      Sets the category that this descriptor depends on. Ignored if `data` is present.        | no, if `data` is present. | 
|`dependentData`|an Object{} with mappings to lists[] of "strings"| Each name in this json object should correspond to a description part provided by a descriptor whose `category` = this descriptor's `dependentCategory`. Each list of strings will be chosen by the selection method if the `dependentCategory` in the artifact exists and gives the `named` value. See the Example.json file| no, if `data is present`. Must be present if `dependentCategory` is|
|   `exclusive`   |  a list[] of "Strings"     |  Adds a filter to the generator that will exclude all categories except the ones listed.         | no | 
|`selector`|a json object (see below)| sets how this descriptor will choose its data.| no |


In addition, a `selector` object can be specified, which determines how the descriptor picks which data it will include in the artifact whenever one is generated:  

|Name|Value|description|Conditions|is required?|
|---|---|---|---| -- |
|`min`|integer| the minimum number of selections to include in the description.| must be nonnegative.| yes |
|`max`|integer| the maximum number of parts of data to include in the description. | must be greater than min. | yes |
|`probability`| floating point| the probability that this selector will choose another piece of data after the minimum number have been accepted.| should be between 0.0 and 1.0|
|`multiplier`|floating point| a value that probability will be multiplied by after each time it chooses to select another piece of data.| should be nonnegative.|
|`withReplacement`|boolean| determines whether or not this selector should replace values back into the possible pieces of data to select from.| when false, `max` should be no more than the size of `data` or the lists in `dependentData`.|


#### Example.json
```json
[
  {
    "category": "Category",
    "data": [
      "Result 1",
      "Result 2"
    ]
  },
  {
    "category": "Dependent Category",
    "dependentCategory": "Category",
    "dependentData": {
      "Result 1": [
        "Result A",
        "Result B"
      ],
      "Result 2": [
        "Result C",
        "Result D"
      ]
    }
  },
  {
    "category": "Exclusive Category",
    "data": [
      "Result Q",
      "Result X"
    ],
    "exclusive": [
      "Dependent Category",
      "Exclusive Category",
      "Selector Category"
    ]
  },
  {
    "category": "Selector Category",
    "data": [
      "Result QRZ",
      "Result ABC",
      "Result DEF",
      "Result GHI"
    ],
    "selector": {
      "min": 1,
      "max": 4,
      "probability": 1.0,
      "multiplier": 0.25,
      "withReplacement": false
    }
  }
]
```