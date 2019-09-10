function watchVideo(filename)
%WATCHVIDEO Mostra un video.
%  INPUT
%   filename: Nome del file video da mostrare;
%  OUTPUT
%   void
% 

% Carico il video con il nome dato
vidObj = VideoReader(filename);
figure;
t=0;

% Scorro tutti i frame del video finchè il video ne ha. Mostro ogni frame
% con il relativo tempo di progressione.
while hasFrame(vidObj)
    t=t+1;
    imshow(readFrame(vidObj)); title(strcat('Time: ', num2str(int8(vidObj.CurrentTime / 60)), ':', num2str(mod(vidObj.CurrentTime,60))));
    pause(1/vidObj.FrameRate);
end

end

