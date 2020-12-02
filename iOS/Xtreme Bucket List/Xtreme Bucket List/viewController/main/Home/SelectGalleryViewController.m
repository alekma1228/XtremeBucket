//
//  SelectGalleryViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "SelectGalleryViewController.h"

@interface SelectGalleryViewController ()

@end

@implementation SelectGalleryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)ontakePhoto:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectGalleryViewControllerDelegate_didSelect:GALLERY_TYPE_CAMERA];
    }];
}
- (IBAction)ontakeGallery:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectGalleryViewControllerDelegate_didSelect:GALLERY_TYPE_GALLERY];
    }];
}
- (IBAction)ontakeWeb:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectGalleryViewControllerDelegate_didSelect:GALLERY_TYPE_WEB];
    }];
}
- (IBAction)ontakeCancel:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:nil];
}

@end
