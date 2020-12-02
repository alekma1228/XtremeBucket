//
//  Landing2ViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "Landing2ViewController.h"
#import "Landing3ViewController.h"
#import "ActionViewController.h"

@interface Landing2ViewController ()

@end

@implementation Landing2ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UISwipeGestureRecognizer * swipleft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeLeft:)];
    swipleft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:swipleft];
    
    UISwipeGestureRecognizer * swipRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeRight:)];
    swipRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:swipRight];
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
- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)onSkip:(id)sender {
    ActionViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ActionViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onNext:(id)sender {
    Landing3ViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"Landing3ViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (void)swipeLeft:(UISwipeGestureRecognizer*)gesture
{
    [self onNext:nil];
}
- (void)swipeRight:(UISwipeGestureRecognizer*)gesture
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
