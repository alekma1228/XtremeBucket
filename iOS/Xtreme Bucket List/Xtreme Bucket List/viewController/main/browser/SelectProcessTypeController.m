//
//  SelectProcessTypeController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/3/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "SelectProcessTypeController.h"

@interface SelectProcessTypeController ()

@end

@implementation SelectProcessTypeController

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

- (IBAction)onOpen:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectProcessTypeController_didSelect:GALLERY_TYPE_OPEN];
    }];
}
- (IBAction)onOpenInNewTab:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectProcessTypeController_didSelect:GALLERY_TYPE_NEWOPEN];
    }];
}
- (IBAction)onCopy:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectProcessTypeController_didSelect:GALLERY_TYPE_COPY];
    }];
}
- (IBAction)onCancel:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
    }];
}
@end
