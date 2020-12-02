//
//  HomeDetailViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "HomeDetailViewController.h"
#import "SelectGalleryViewController.h"
#import "BIZPopupViewController.h"
#import "SearchImageViewController.h"

@interface HomeDetailViewController ()<SelectGalleryViewControllerDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, SearchImageViewControllerDelegate>
{
    BOOL isCamera;
    BOOL isGallery;
    BOOL isWeb;
    BOOL hasPhoto;
}

@property (weak, nonatomic) IBOutlet UITextField *edt_goal;
@property (weak, nonatomic) IBOutlet UIImageView *img_thumb;
@property (weak, nonatomic) IBOutlet UIImageView *img_addBtn;
@property (weak, nonatomic) IBOutlet IQTextView *txt_detail;
@property (weak, nonatomic) IBOutlet IQTextView *txt_note;

@property (nonatomic, retain) UIImagePickerController * picker;
@end

@implementation HomeDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.txt_detail.placeholder = @"Enter Your Details";
    self.txt_note.placeholder = @"Enter Your Noted Here";
    
    if(self.detail.length > 0){
        self.txt_detail.text = self.detail;
    }
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
- (IBAction)onSelectPicture:(id)sender {
    SelectGalleryViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SelectGalleryViewController"];
    BIZPopupViewController *popUp = [[BIZPopupViewController alloc] initWithContentViewController:controller contentSize:CGSizeMake(300, 240)];
    controller.parent = popUp;
    controller.delegate = self;
    [self presentViewController:popUp animated:YES completion:nil];
}

//progmark select gallery delegate
- (void) SelectGalleryViewControllerDelegate_didSelect:(int)galleryType
{
    if(galleryType == GALLERY_TYPE_CAMERA){
        [self onTakePhoto:nil];
    }else if(galleryType == GALLERY_TYPE_GALLERY){
        [self onChoosePhoto:nil];
    }else if(galleryType == GALLERY_TYPE_WEB){
        SearchImageViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SearchImageViewController"];
        controller.delegate = self;
        [self.navigationController pushViewController:controller animated:YES];
    }
}

- (void) SearchImageViewControllerDelegate_didSelectImage:(NSDictionary *)dataDict
{
    _img_addBtn.hidden = YES;
    NSString * imageUrl = dataDict[@"link"];
    [self.img_thumb sd_setImageWithURL:[NSURL URLWithString:imageUrl] placeholderImage:[UIImage imageNamed:@""]];
}


- (void)onChoosePhoto:(id)sender {
    if (![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    isGallery = YES;
    isCamera = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void)onTakePhoto:(id)sender {
    if (![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
    isCamera = YES;
    isGallery = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    UIImage *image = (UIImage *)[info valueForKey:UIImagePickerControllerOriginalImage];
    hasPhoto = YES;
    [_img_thumb setImage:image];
    _img_addBtn.hidden = YES;
}
- (void) imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
}
- (IBAction)onSave:(id)sender {
    if(self.edt_goal.text.length == 0){
        [Util showAlertTitle:self title:@"Error" message:@"Please enter your goal."];
    }else if(!self.img_thumb.image){
        [Util showAlertTitle:self title:@"Error" message:@"Please select item image."];
    }else if(self.txt_detail.text.length == 0){
        [Util showAlertTitle:self title:@"Error" message:@"Please input detail."];
    }else if(self.txt_note.text.length == 0){
        [Util showAlertTitle:self title:@"Error" message:@"Please input note."];
    }else{
        PFObject * item = [PFObject objectWithClassName:PARSE_TABLE_BUCKETLIST];
        item[PARSE_BUCKETLIST_GOAL] = self.edt_goal.text;
        item[PARSE_BUCKETLIST_NOTE] = self.txt_note.text;
        item[PARSE_BUCKETLIST_DETAIL] = self.txt_detail.text;
        item[PARSE_BUCKETLIST_ISCOMPLETE] = [NSNumber numberWithBool:NO];
        item[PARSE_BUCKETLIST_OWNER] = [PFUser currentUser];
        
        UIImage *profileImage = [Util getUploadingImageFromImage:_img_thumb.image];
        NSData *imageData = UIImageJPEGRepresentation(profileImage, 0.8);
        item[PARSE_BUCKETLIST_PICTURE] = [PFFile fileWithData:imageData];
        [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
        [item saveInBackgroundWithBlock:^(BOOL success, NSError * error){
            [SVProgressHUD dismiss];
            if(error){
                [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
            }else{
                [Util showAlertTitle:self title:@"Success" message:@"Bucket created." finish:^{
                    [self.navigationController popViewControllerAnimated:YES];
                }];
            }
        }];
    }
}
@end
