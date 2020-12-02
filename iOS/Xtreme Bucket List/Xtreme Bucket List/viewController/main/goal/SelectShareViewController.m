//
//  SelectShareViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "SelectShareViewController.h"

@interface SelectShareViewController ()

@end

@implementation SelectShareViewController

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
- (IBAction)onSelectFaceBook:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectShareViewControllerDelegate_didSelect:SHARE_MODE_FACEBOOK];
    }];
}
- (IBAction)onSelectInstagram:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectShareViewControllerDelegate_didSelect:SHARE_MODE_INSTAGRAM];
    }];
}
- (IBAction)onSelectTwitter:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectShareViewControllerDelegate_didSelect:SHARE_MODE_TWEETER];
    }];
}
- (IBAction)onSelectCopyLink:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:^{
        [self.delegate SelectShareViewControllerDelegate_didSelect:SHARE_MODE_COPYLINK];
    }];
}
- (IBAction)onSelectCancel:(id)sender {
    [self.parent dismissPopupViewControllerAnimated:YES completion:nil];
}

@end
