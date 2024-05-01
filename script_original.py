import cv2
import os
import sys
from ultralytics import YOLO
from moviepy.editor import VideoFileClip, AudioFileClip

def blur_person_area(frame, x1, y1, x2, y2):
    blur_value = (23, 23)  # Adjust the blur value here
    x1, y1, x2, y2 = int(x1), int(y1), int(x2), int(y2)
    frame[y1:y2, x1:x2] = cv2.GaussianBlur(frame[y1:y2, x1:x2], blur_value, 0)
    return frame

def run_yolo(model_choice, video_path):
    if model_choice == 1:
        model = YOLO('best_without_interview.pt')
    else:
        model = YOLO('best_with_interview.pt')

    cap = cv2.VideoCapture(video_path)
    frame_width = int(cap.get(3))
    frame_height = int(cap.get(4))
    frame_rate = int(cap.get(5))
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    output_without_sound_path = "output_without_sound.mp4"
    output_path = "output_video.mp4"
    out = cv2.VideoWriter(output_without_sound_path, fourcc, frame_rate, (frame_width, frame_height))

    while cap.isOpened():
        success, frame = cap.read()
        if success:
            results = model.track([frame], tracker="botsort.yaml",persist=True, classes=[1])  # Track only "person" class
            for result in results:
                for bbox in result.boxes.xyxy:
                    x1, y1, x2, y2 = bbox[0].item(), bbox[1].item(), bbox[2].item(), bbox[3].item()
                    frame = blur_person_area(frame, x1, y1, x2, y2)
            out.write(frame)
            cv2.imshow("YOLOv8 Person Blurring", frame)
            if cv2.waitKey(1) & 0xFF == ord("q"):
                break
        else:
            break

    cap.release()
    out.release()
    cv2.destroyAllWindows()

    output = VideoFileClip(output_without_sound_path)
    audio_clip = AudioFileClip(video_path)
    video_clip = output.set_audio(audio_clip)
    video_clip.write_videofile(output_path, codec='libx264')

    try:
        os.remove(output_without_sound_path)
    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python script.py <model_choice> <video_path>")
        sys.exit(1)

    model_choice = int(sys.argv[1])
    video_path = sys.argv[2]

    run_yolo(model_choice, video_path)