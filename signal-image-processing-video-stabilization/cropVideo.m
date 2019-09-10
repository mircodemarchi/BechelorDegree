function cropVideo(filename_in, framename_out, from_min, from_sec, to_min, to_sec)
%CROPVIDEO Taglia un video in un intervallo di minuti e secondi
%          specificato.
%  INPUT
%   filename_in: Nome del file video da tagliare;
%   framename_out: Nome del video di frame tagliato;
%   from_min: Minuto dal quale tagliare;
%   from_sec: Secondo dal quale tagliare;
%   to_min: Minuto fino al quale tagliare;
%   to_sec: Secondo fino al quale tagliare;
%  OUTPUT
%   void
%

% Carico il video con il filename dato
vidObj = VideoReader(filename_in);

% Check su from_min:from_sec <= to_min:to_sec
% Check sui valori nell'intervallo [0,59]
if from_min > to_min || from_min > 59 || to_min > 59 || from_sec > 59 || to_sec > 59 || from_min < 0 || to_min < 0 || from_sec < 0 || to_sec < 0
    return
else
    if from_min == to_min && from_sec > to_sec
        return
    end
end

% Trasformo i minuti in secondi
from_time = (from_min * 60) + from_sec
to_time = (to_min * 60) + to_sec

% Skippo il video fino al tempo in cui voglio iniziare il taglio
vidObj.CurrentTime = from_time;

tt = 0;

% Memorizzo su vidFrame tutti i frame che voglio tenere e li mostro
figure;
while vidObj.CurrentTime < to_time
    tt=tt+1;
    vidFrame(:,:,:,tt) = readFrame(vidObj);
    imshow(vidFrame(:,:,:,tt)); title(strcat('Time: ', num2str(int8(vidObj.CurrentTime / 60)), ':', num2str(mod(vidObj.CurrentTime,60))));
    pause(1/vidObj.FrameRate);
end

% Salvo il video in frames con il nome specificato
frames = vidFrame;
save(framename_out, 'frames');

end

