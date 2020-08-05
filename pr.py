import cv2

vidcap = cv2.VideoCapture('movie/test.mp4')
count=0
while(vidcap.isOpened()):
    if count%20 == 0 :
        ret,image = vidcap.read()
        cv2.imwrite('C:\\images\\frame%d.jpg' % count,image)
        print('Saved frame%d.jpg' % count)
        count += 1

vidcap.release()